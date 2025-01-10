package ecommerce.coupang.service.discount;

import ecommerce.coupang.service.store.CouponService;
import org.springframework.stereotype.Service;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

	private final CouponService couponService;
	private final List<DiscountPolicy> discountPolicies;

	/**
	 * 모든 할인 정책에 대해 할인 금액 계산
	 */
	public int calculateTotalDiscount(int totalPrice, Member member, MemberCoupon memberCoupon) {
		return discountPolicies.stream()
				.mapToInt(policy -> policy.calculateDiscount(totalPrice, member, memberCoupon))
				.sum(); // 각 정책의 할인 금액을 합산
	}

	/* coupon 사용시 다운받은 쿠폰 가져오기 */
	public MemberCoupon getMemberCouponIfPresent(Member member, Long couponId) throws CustomException {
		if (couponId == null) return null;

		return couponService.getMemberCoupon(member, couponId);
	}
}
