package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.domain.store.Coupon;

public interface DiscountPolicy {

	int calculateDiscount(int price, MemberGrade memberGrade, Coupon coupon);
}
