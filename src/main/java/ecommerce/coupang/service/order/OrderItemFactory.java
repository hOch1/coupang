package ecommerce.coupang.service.order;

import org.springframework.stereotype.Component;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemFactory {

	private final DiscountService discountService;

	public OrderItem createOrderItem(Order order, ProductVariant productVariant, Member member, int quantity, Long couponId) throws CustomException {
		int totalPrice = productVariant.getPrice() * quantity;

		// 할인 계산
		MemberCoupon memberCoupon = discountService.getMemberCouponIfPresent(member, couponId);
		int totalDiscountPrice = discountService.calculateTotalDiscount(totalPrice, member, memberCoupon);

		OrderItem orderItem = OrderItem.create(order, productVariant, memberCoupon, quantity, totalDiscountPrice);
		/* 배송 연결 */
		Delivery delivery = Delivery.create(orderItem);
		orderItem.setDelivery(delivery);

		return orderItem;
	}
}
