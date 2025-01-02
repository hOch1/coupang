package ecommerce.coupang.dto.response.store.coupon;

import java.time.LocalDateTime;

import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.domain.store.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponResponse {

	private final Long id;
	private final String name;
	private final int minPrice;
	private final LocalDateTime limitDate;
	private final CouponType type;
	private final Integer discountValue;
	private final Integer limitDiscountPrice;

	public static CouponResponse from(CouponProduct couponProduct) {
		Coupon coupon = couponProduct.getCoupon();
		return new CouponResponse(
			coupon.getId(),
			coupon.getName(),
			coupon.getMinPrice(),
			coupon.getLimitDate().equals(LocalDateTime.MAX) ? null : coupon.getLimitDate(),
			coupon.getType(),
			coupon.getDiscountValue(),
			coupon.getLimitDiscountPrice() == Integer.MAX_VALUE ? null : coupon.getLimitDiscountPrice()
		);
	}

	public static CouponResponse from(Coupon coupon) {
		return new CouponResponse(
			coupon.getId(),
			coupon.getName(),
			coupon.getMinPrice(),
			coupon.getLimitDate().equals(LocalDateTime.MAX) ? null : coupon.getLimitDate(),
			coupon.getType(),
			coupon.getDiscountValue(),
			coupon.getLimitDiscountPrice() == Integer.MAX_VALUE ? null : coupon.getLimitDiscountPrice()
		);
	}

	public static CouponResponse from(MemberCoupon memberCoupon) {
		Coupon coupon = memberCoupon.getCoupon();
		return new CouponResponse(
			memberCoupon.getId(),
			coupon.getName(),
			coupon.getMinPrice(),
			coupon.getLimitDate().equals(LocalDateTime.MAX) ? null : coupon.getLimitDate(),
			coupon.getType(),
			coupon.getDiscountValue(),
			coupon.getLimitDiscountPrice() == Integer.MAX_VALUE ? null : coupon.getLimitDiscountPrice()
		);
	}
}
