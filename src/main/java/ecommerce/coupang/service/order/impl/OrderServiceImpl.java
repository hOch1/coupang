package ecommerce.coupang.service.order.impl;


import ecommerce.coupang.domain.member.MemberCoupon;
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
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.member.MemberCouponRepository;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.repository.store.CouponRepository;
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

		int couponDiscountPrice = 0;
		int memberDiscountPrice;
		MemberCoupon memberCoupon = null;
		if (request.getCouponId() != null){
			memberCoupon = memberCouponRepository.findByMemberIdAndCouponId(member.getId(), request.getCouponId())
				.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
			if (memberCoupon.isUsed())
				throw new CustomException(ErrorCode.ALREADY_USE_COUPON);
			couponDiscountPrice = couponDiscountPolicy.calculateDiscount(productVariant.getPrice() * request.getQuantity(), null, memberCoupon.getCoupon());
		}
		memberDiscountPrice = memberDiscountPolicy.calculateDiscount(productVariant.getPrice() * request.getQuantity(), member.getGrade(), null);

		OrderItem orderItem = OrderItem.createByProduct(order, productVariant, memberCoupon.getCoupon(), request, couponDiscountPrice, memberDiscountPrice);
		memberCoupon.use();
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

			int couponDiscountPrice = 0;
			int memberDiscountPrice;
			MemberCoupon memberCoupon = null;
			if (cartItemRequest.getCouponId() != null){
				memberCoupon = memberCouponRepository.findByMemberIdAndCouponId(member.getId(), cartItemRequest.getCouponId())
					.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
				couponDiscountPrice = couponDiscountPolicy.calculateDiscount(cartItem.getProductVariant().getPrice() * cartItem.getQuantity(), null, memberCoupon.getCoupon());
			}

			memberDiscountPrice = memberDiscountPolicy.calculateDiscount(cartItem.getProductVariant().getPrice() * cartItem.getQuantity(), member.getGrade(), null);

			OrderItem orderItem = OrderItem.createByCartItem(order, cartItem, memberCoupon.getCoupon(), couponDiscountPrice, memberDiscountPrice);
			memberCoupon.use();
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
}
