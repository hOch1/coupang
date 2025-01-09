package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;

public interface DiscountPolicy {

	int calculateDiscount(int price, Member member, MemberCoupon memberCoupon);
}
