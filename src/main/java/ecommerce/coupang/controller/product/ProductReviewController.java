package ecommerce.coupang.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.product.review.CreateReviewRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.product.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ProductReviewController {

	private final ProductReviewService productReviewService;

	@PostMapping("/{productId}")
	@Operation(summary = "리뷰 등록 API", description = "리뷰를 등록합니다.")
	public ResponseEntity<Void> createReview(
		@PathVariable Long productId,
		@RequestBody CreateReviewRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productReviewService.createReview(productId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/{reviewId}/like")
	@Operation(summary = "리뷰 좋아요 API", description = "리뷰에 좋아요를 추가합니다. (이미 좋아요한 경우 취소)")
	public ResponseEntity<Void> likeReview(
		@PathVariable Long reviewId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productReviewService.likeReview(reviewId, userDetails.getMember());
		return ResponseEntity.ok().build();
	}
}
