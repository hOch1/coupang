package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.Member;

public interface DiscountPolicy {

	int calculateDiscount(int price, Member member);
}
