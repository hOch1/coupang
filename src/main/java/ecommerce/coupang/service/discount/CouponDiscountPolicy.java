package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import org.springframework.stereotype.Component;

import ecommerce.coupang.domain.store.Coupon;

@Component
public class CouponDiscountPolicy implements DiscountPolicy{

	@Override
	public int calculateDiscount(int price, Member member, MemberCoupon memberCoupon) {
		if (memberCoupon == null) return 0;

		Coupon coupon = memberCoupon.getCoupon();

		if (coupon.validateMinPrice(price)) return 0;

		int discountPrice = coupon.calculateDiscount(price);
		memberCoupon.use();

		return Math.min(discountPrice, coupon.getLimitDiscountPrice());
	}
}
