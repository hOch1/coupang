package ecommerce.coupang.dto.request.product.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReviewRequest {

	@NotBlank(message = "내용을 입력해주세요.")
	private final String content;

	@Min(value = 1, message = "별점은 최소 1 이상이어야 합니다.")
	@Max(value = 5, message = "별점은 최대 5 이하이어야 합니다.")
	private final int star;
}
