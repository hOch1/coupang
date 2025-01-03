package ecommerce.coupang.dto.response.store.coupon;

import java.time.LocalDateTime;

import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCouponResponse {

	private final Long id;
	private final Long couponId;
	private final String name;
	private final int minPrice;
	private final LocalDateTime limitDate;
	private final CouponType type;
	private final Integer discountValue;
	private final Integer limitDiscountPrice;
	private final boolean isUsed;

	public static MemberCouponResponse from(MemberCoupon memberCoupon) {
		Coupon coupon = memberCoupon.getCoupon();

		return new MemberCouponResponse(
			memberCoupon.getId(),
			coupon.getId(),
			coupon.getName(),
			coupon.getMinPrice(),
			coupon.getLimitDate().equals(LocalDateTime.MAX) ? null : coupon.getLimitDate(),
			coupon.getType(),
			coupon.getDiscountValue(),
			coupon.getLimitDiscountPrice() == Integer.MAX_VALUE ? null : coupon.getLimitDiscountPrice(),
			memberCoupon.isUsed()
		);
	}
}
