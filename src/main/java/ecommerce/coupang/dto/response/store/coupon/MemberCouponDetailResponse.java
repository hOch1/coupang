package ecommerce.coupang.dto.response.store.coupon;

import java.time.LocalDateTime;

import ecommerce.coupang.domain.store.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCouponDetailResponse {

	private final Long id;
	private final Long couponId;
	private final String name;
	private final int minPrice;
	private final LocalDateTime limitDate;
	private final CouponType type;
	private final Integer discountValue;
	private final Integer limitDiscountPrice;
	private final boolean isUsed;
	private final LocalDateTime createdAt;
	private final LocalDateTime usedAt;


}
