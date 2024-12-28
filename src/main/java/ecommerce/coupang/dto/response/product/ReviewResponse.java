package ecommerce.coupang.dto.response.product;

import java.time.LocalDateTime;

import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.dto.response.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponse {

	private final Long id;
	private final Long productId;
	private final MemberResponse member;
	private final String content;
	private final int star;
	private final int likes;
	private final LocalDateTime createdAt;

	public static ReviewResponse from(ProductReview productReview) {
		return new ReviewResponse(
			productReview.getId(),
			productReview.getProduct().getId(),
			MemberResponse.from(productReview.getMember()),
			productReview.getContent(),
			productReview.getStar(),
			productReview.getLikes().size(),
			productReview.getCreatedAt()
		);
	}
}
