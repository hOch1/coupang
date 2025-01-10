package ecommerce.coupang.service.discount;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;

public interface DiscountPolicy {

	/**
	 * 할인 금액 계산
	 * @param price 상품 총 가격
	 * @param member 요청한 회원
	 * @param memberCoupon 사용할 쿠폰 [null 가능]
	 * @return 할인 금액
	 */
	int calculateDiscount(int price, Member member, MemberCoupon memberCoupon);
}
