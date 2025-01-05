package ecommerce.coupang.service.order.impl;


import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.domain.order.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.member.MemberCouponRepository;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.discount.DiscountPolicy;
import ecommerce.coupang.service.member.query.AddressQueryService;
import ecommerce.coupang.service.order.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductVariantRepository productVariantRepository;
	private final CartItemRepository cartItemRepository;
	private final AddressQueryService addressQueryService;
	private final MemberCouponRepository memberCouponRepository;
	private final DiscountPolicy couponDiscountPolicy;
	private final DiscountPolicy memberDiscountPolicy;

	@Override
	@Transactional
	public Order createOrderByProduct(CreateOrderByProductRequest request, Member member) throws CustomException {
		Address address = addressQueryService.getAddress(request.getAddressId());
		Order order = Order.createByProduct(request, member, address);

		ProductVariant productVariant = productVariantRepository.findByIdWithStore(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		verifyStatusAndReduceStock(productVariant, request.getQuantity());

		int totalPrice = productVariant.getPrice() * request.getQuantity();
		MemberCoupon memberCoupon = getMemberCouponIfPresent(member, request.getCouponId());
		int couponDiscountPrice = couponDiscount(memberCoupon, totalPrice);
		int memberDiscountPrice = memberDiscount(member.getGrade(), totalPrice);

		OrderItem orderItem = OrderItem.createByProduct(order, productVariant, memberCoupon, request, couponDiscountPrice, memberDiscountPrice);
		Delivery delivery = Delivery.create(orderItem, productVariant.getProduct().getStore());
		orderItem.setDelivery(delivery);

		order.addOrderItem(orderItem);

		orderRepository.save(order);

		return order;
	}


	@Override
	@Transactional
	public Order createOrderByCart(CreateOrderByCartRequest request, Member member) throws CustomException {
		Address address = addressQueryService.getAddress(request.getAddressId());
		Order order = Order.createByCart(request, member, address);

		for (CreateOrderByCartRequest.CartItemRequest cartItemRequest : request.getCartItems()) {
			CartItem cartItem = cartItemRepository.findByIdWithStore(cartItemRequest.getCartItemId())
				.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

			verifyStatusAndReduceStock(cartItem.getProductVariant(), cartItem.getQuantity());

			int totalPrice = cartItem.getProductVariant().getPrice() * cartItem.getQuantity();
			MemberCoupon memberCoupon = getMemberCouponIfPresent(member, cartItemRequest.getCouponId());
			int couponDiscountPrice = couponDiscount(memberCoupon, totalPrice);
			int memberDiscountPrice = memberDiscount(member.getGrade(), totalPrice);

			OrderItem orderItem = OrderItem.createByCartItem(order, cartItem, memberCoupon, couponDiscountPrice, memberDiscountPrice);
			Delivery delivery = Delivery.create(orderItem, cartItem.getProductVariant().getProduct().getStore());
			orderItem.setDelivery(delivery);

			order.addOrderItem(orderItem);
			cartItemRepository.delete(cartItem);
		}

		orderRepository.save(order);
		return order;
	}

	@Override
	@Transactional
	public Order cancelOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findByIdWithMemberAndAddress(orderId).orElseThrow(() ->
			new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		if (order.getStatus().equals(OrderStatus.CANCELLED))
			throw new CustomException(ErrorCode.ALREADY_CANCELLED_ORDER);

		order.cancel();
		return order;
	}

	private void verifyStatusAndReduceStock(ProductVariant productVariant, int quantity) throws CustomException {
		if (!productVariant.getStatus().equals(ProductStatus.ACTIVE))
			throw new CustomException(ErrorCode.PRODUCT_STATUS_NOT_ACTIVE);

		productVariant.reduceStock(quantity);
	}

	private MemberCoupon getMemberCouponIfPresent(Member member, Long couponId) throws CustomException {
		if (couponId == null)
			return null;

		return memberCouponRepository.findByMemberIdAndCouponId(member.getId(), couponId)
				.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
	}

	private int couponDiscount(MemberCoupon memberCoupon, int price) throws CustomException {
		if (memberCoupon == null)
			return 0;
		if (memberCoupon.isUsed())
			throw new CustomException(ErrorCode.ALREADY_USE_COUPON);

		memberCoupon.use();
		return couponDiscountPolicy.calculateDiscount(price, null, memberCoupon.getCoupon());
	}

	private int memberDiscount(MemberGrade memberGrade, int price) {
		return memberDiscountPolicy.calculateDiscount(price, memberGrade, null);
	}
}
