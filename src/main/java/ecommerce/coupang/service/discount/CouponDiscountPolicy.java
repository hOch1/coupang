package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import org.springframework.stereotype.Component;

import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.domain.store.Coupon;

@Component("couponDiscountPolicy")
public class CouponDiscountPolicy implements DiscountPolicy{

	@Override
	public int calculateDiscount(int price, Member member, MemberCoupon memberCoupon) {
		int discountPrice = 0;

		if (memberCoupon == null) return discountPrice;
		Coupon coupon = memberCoupon.getCoupon();

		if (price < coupon.getMinPrice()) return discountPrice;

		switch (coupon.getType()) {
			case FIXED -> discountPrice = coupon.getDiscountValue();
			case PERCENT -> discountPrice = (int)(price * coupon.getDiscountValue() / 100.0);
		}

		memberCoupon.use();

		return Math.min(discountPrice, coupon.getDiscountValue());
	}
}
