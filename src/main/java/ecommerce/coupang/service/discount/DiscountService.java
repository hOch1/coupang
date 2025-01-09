package ecommerce.coupang.service.discount;

import org.springframework.stereotype.Service;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.repository.member.MemberCouponRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountService {

	private final DiscountPolicy memberDiscountPolicy;
	private final DiscountPolicy couponDiscountPolicy;

	public int calculateCouponDiscount(MemberCoupon memberCoupon, int price) throws CustomException {
		if (memberCoupon == null) return 0;
		if (memberCoupon.isUsed())
			throw new CustomException(ErrorCode.ALREADY_USE_COUPON);

		memberCoupon.use();
		return couponDiscountPolicy.calculateDiscount(price, null, memberCoupon.getCoupon());
	}

	public int calculateMemberDiscount(MemberGrade memberGrade, int price) {
		return memberDiscountPolicy.calculateDiscount(price, memberGrade, null);
	}
}
