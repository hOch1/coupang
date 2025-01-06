package ecommerce.coupang.service.order.impl;

import ecommerce.coupang.common.utils.ProductUtils;
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
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.discount.DiscountService;
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
	private final DiscountService discountService;

	@Override
	@Transactional
	public Order createOrderByProduct(CreateOrderByProductRequest request, Member member) throws CustomException {
		Address address = addressQueryService.getAddress(request.getAddressId());
		Order order = Order.createByProduct(request, member, address);

		ProductVariant productVariant = productVariantRepository.findByIdWithStore(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		ProductUtils.verifyStatusAndReduceStock(productVariant, request.getQuantity());
		OrderItem orderItem = createOrderItem(order, productVariant, member, request.getQuantity(), request.getCouponId());

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

			ProductUtils.verifyStatusAndReduceStock(cartItem.getProductVariant(), cartItem.getQuantity());
			OrderItem orderItem = createOrderItem(order, cartItem.getProductVariant(), member, cartItem.getQuantity(), cartItemRequest.getCouponId());

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

	private OrderItem createOrderItem(Order order, ProductVariant productVariant, Member member, int quantity, Long couponId) throws CustomException {
		int totalPrice = productVariant.getPrice() * quantity;

		// 할인 계산
		MemberCoupon memberCoupon = discountService.getMemberCouponIfPresent(member, couponId);
		int couponDiscountPrice = discountService.calculateCouponDiscount(memberCoupon, totalPrice);
		int memberDiscountPrice = discountService.calculateMemberDiscount(member.getGrade(), totalPrice);

		// 주문 상품 생성
		OrderItem orderItem = OrderItem.create(order, productVariant, memberCoupon, quantity, couponDiscountPrice, memberDiscountPrice);
		Delivery delivery = Delivery.create(orderItem, productVariant.getProduct().getStore());
		orderItem.setDelivery(delivery);

		return orderItem;
	}
}
