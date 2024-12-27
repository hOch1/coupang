package ecommerce.coupang.dto.request.product.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReviewRequest {

	private final String content;
	private final int star;
}
