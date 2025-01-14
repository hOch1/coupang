package ecommerce.coupang.service.discount;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.member.MemberGrade;

class MemberDiscountPolicyTest {

	private final MemberDiscountPolicy memberDiscountPolicy = new MemberDiscountPolicy();

	private Member member = mock(Member.class);

	@Test
	@DisplayName("회원 등급 할인 테스트 - 일반")
	void calculateDiscount_normal() {
		int price = 1000;
		when(member.getGrade()).thenReturn(MemberGrade.NORMAL);

		int discount = memberDiscountPolicy.calculateDiscount(price, member, null);

		assertThat(discount).isEqualTo(0);
	}

	@Test
	@DisplayName("회원 등급 할인 테스트 - 골드")
	void calculateDiscount_gold() {
		int price = 1000;
		when(member.getGrade()).thenReturn(MemberGrade.GOLD);

		int discount = memberDiscountPolicy.calculateDiscount(price, member, null);

		assertThat(discount).isEqualTo(5);
	}

	@Test
	@DisplayName("회원 등급 할인 테스트 - VIP")
	void calculateDiscount_vip() {
		int price = 1000;
		when(member.getGrade()).thenReturn(MemberGrade.VIP);

		int discount = memberDiscountPolicy.calculateDiscount(price, member, null);

		assertThat(discount).isEqualTo(10);
	}

	@Test
	@DisplayName("회원 등급 할인 테스트 - VVIP")
	void calculateDiscount_vvip() {
		int price = 1000;
		when(member.getGrade()).thenReturn(MemberGrade.VVIP);

		int discount = memberDiscountPolicy.calculateDiscount(price, member, null);

		assertThat(discount).isEqualTo(15);
	}
}