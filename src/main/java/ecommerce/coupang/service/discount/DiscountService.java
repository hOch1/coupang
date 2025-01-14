package ecommerce.coupang.service.discount;

import org.springframework.stereotype.Service;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

	private final List<DiscountPolicy> discountPolicies;

	/**
	 * 모든 할인 정책에 대해 할인 금액 계산
	 */
	public int calculateTotalDiscount(int totalPrice, Member member, MemberCoupon memberCoupon) {
		return discountPolicies.stream()
				.mapToInt(policy -> policy.calculateDiscount(totalPrice, member, memberCoupon))
				.sum(); // 각 정책의 할인 금액을 합산
	}
}
