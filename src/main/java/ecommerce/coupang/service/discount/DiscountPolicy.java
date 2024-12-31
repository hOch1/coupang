package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.StoreCoupon;

public interface DiscountPolicy {

	int calculateDiscount(int price, Member member, StoreCoupon coupon);
}
