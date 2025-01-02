package ecommerce.coupang.dto.request.store.coupon;

import java.time.LocalDateTime;

import ecommerce.coupang.domain.store.CouponType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCouponRequest {

	@NotBlank(message = "쿠폰 이름을 입력해주세요")
	private final String name;

	private final String description;

	@NotNull(message = "쿠폰 할인 타입을 입력해주세요")
	private final CouponType type;

	@Min(value = 1, message = "할인 금액(비율)은 1 이상이어야 합니다.")
	private final int discountValue;

	private final Integer minPrice;
	private final Integer limitDiscountPrice;
	private final Integer couponStock;
	private final LocalDateTime limitDate;
}
