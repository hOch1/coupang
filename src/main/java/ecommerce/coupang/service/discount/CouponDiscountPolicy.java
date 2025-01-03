package ecommerce.coupang.service.discount;

import org.springframework.stereotype.Component;

import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.domain.store.Coupon;

@Component("couponDiscountPolicy")
public class CouponDiscountPolicy implements DiscountPolicy{

	@Override
	public int calculateDiscount(int price, MemberGrade memberGrade, Coupon coupon) {
		if (price < coupon.getMinPrice()) return 0;

		int discountPrice = 0;
		switch (coupon.getType()) {
			case FIXED -> discountPrice = coupon.getDiscountValue();
			case PERCENT -> discountPrice = (int)(price * coupon.getDiscountValue() / 100.0);
		}

		return Math.min(discountPrice, coupon.getDiscountValue());
	}
}
