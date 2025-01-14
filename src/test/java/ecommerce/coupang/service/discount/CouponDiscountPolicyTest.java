package ecommerce.coupang.service.discount;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;

class CouponDiscountPolicyTest {

	private final CouponDiscountPolicy couponDiscountPolicy = new CouponDiscountPolicy();

	private MemberCoupon memberCoupon = mock(MemberCoupon.class);
	private Coupon coupon = mock(Coupon.class);

	@Test
	@DisplayName("쿠폰 할인 테스트")
	void calculateDiscount() {
		int price = 1000;
		when(memberCoupon.getCoupon()).thenReturn(coupon);

		couponDiscountPolicy.calculateDiscount(price, null, memberCoupon);

		verify(coupon).validateMinPrice(price);
		verify(memberCoupon).use();
	}

	@Test
	@DisplayName("쿠폰 할인 테스트 - (쿠폰 없음)")
	void calculateDiscount_NotUseCoupon() {
		int price = 1000;

		int discount = couponDiscountPolicy.calculateDiscount(price, null, null);

		assertThat(discount).isEqualTo(0);
	}

	@Test
	@DisplayName("쿠폰 할인 테스트 - (최소 금액 충족 안됨)")
	void calculateDiscount_NotOverMinPrice() {
		int price = 1000;
		when(memberCoupon.getCoupon()).thenReturn(coupon);
		when(coupon.validateMinPrice(price)).thenReturn(true);

		int discount = couponDiscountPolicy.calculateDiscount(price, null, memberCoupon);

		assertThat(discount).isEqualTo(0);
	}

	@Test
	@DisplayName("쿠폰 할인 테스트 - (할인 최대 금액보다 높음)")
	void calculateDiscount_OverLimitPrice() {
		int price = 1000;
		when(memberCoupon.getCoupon()).thenReturn(coupon);

		when(coupon.calculateDiscount(price)).thenReturn(100);
		when(coupon.getLimitDiscountPrice()).thenReturn(10);

		int discount = couponDiscountPolicy.calculateDiscount(price, null, memberCoupon);

		assertThat(discount).isEqualTo(10);
	}
}