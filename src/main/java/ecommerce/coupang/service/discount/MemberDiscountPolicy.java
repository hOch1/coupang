package ecommerce.coupang.service.discount;

import org.springframework.stereotype.Component;

import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.domain.store.Coupon;

@Component("memberDiscountPolicy")
public class MemberDiscountPolicy implements DiscountPolicy{

	@Override
	public int calculateDiscount(int price, MemberGrade memberGrade, Coupon coupon) {
		return switch (memberGrade) {
			case GOLD -> (int)(price * 0.005); 	// 0.05%
			case VIP -> (int)(price * 0.01); 	// 0.1%
			case VVIP -> (int)(price * 0.015);	// 0.15%
			default -> 0;
		};
	}
}