package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.Member;

public class MemberDiscountPolicy implements DiscountPolicy{

	@Override
	public int calculateDiscount(int price, Member member) {
		return switch (member.getGrade()) {
			case GOLD -> (int)(price * 0.02);
			case VIP -> (int)(price * 0.05);
			case VVIP -> (int)(price * 0.1);
			default -> 0;
		};
	}
}
