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
import ecommerce.coupang.service.store.CouponService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemFactory {

	private final DiscountService discountService;
	private final CouponService couponService;

	public OrderItem createOrderItem(Order order, ProductVariant productVariant, Member member, int quantity, Long couponId) throws CustomException {
		int totalPrice = productVariant.getPrice() * quantity;

		/* 할인 계산 */
		int totalDiscountPrice;
		MemberCoupon memberCoupon = null;

		if (couponId != null) {
			memberCoupon = couponService.getMemberCoupon(member, couponId);
			memberCoupon.use();
		}

		totalDiscountPrice = discountService.calculateTotalDiscount(totalPrice, member, memberCoupon);

		OrderItem orderItem = OrderItem.of(order, productVariant, memberCoupon, quantity, totalDiscountPrice);
		/* 배송 연결 */
		Delivery delivery = Delivery.from(orderItem);
		orderItem.setDelivery(delivery);

		return orderItem;
	}
}
