package ecommerce.coupang.service.discount;

import org.springframework.stereotype.Component;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.StoreCoupon;

@Component
public class MemberDiscountPolicy implements DiscountPolicy{

	@Override
	public int calculateDiscount(int price, Member member, StoreCoupon coupon) {
		return switch (member.getGrade()) {
			case GOLD -> (int)(price * 0.005); 	// 0.05%
			case VIP -> (int)(price * 0.01); 	// 0.1%
			case VVIP -> (int)(price * 0.015);	// 0.15%
			default -> 0;
		};
	}
}