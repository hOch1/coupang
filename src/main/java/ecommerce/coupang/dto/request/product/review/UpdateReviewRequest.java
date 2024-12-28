package ecommerce.coupang.dto.request.product.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReviewRequest {

	private final String content;
	private final Long star;
}
