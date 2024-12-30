package ecommerce.coupang.controller.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.dto.request.product.review.CreateReviewRequest;
import ecommerce.coupang.dto.request.product.review.ReviewSort;
import ecommerce.coupang.dto.request.product.review.UpdateReviewRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.product.ReviewResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.product.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/reviews")
@RequiredArgsConstructor
@Tag(name = "상품 리뷰 API", description = "상품 리뷰 관련 API")
public class ProductReviewController {

	private final ProductReviewService productReviewService;

	@PostMapping("/{productId}")
	@Operation(summary = "리뷰 등록 API", description = "리뷰를 등록합니다.")
	public ResponseEntity<Void> createReview(
		@PathVariable Long productId,
		@RequestBody @Valid CreateReviewRequest request,
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
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/me")
	@Operation(summary = "내가 작성한 리뷰 조회 API", description = "내가 작성한 리뷰를 조회합니다.")
	public ResponseEntity<Result<List<ReviewResponse>>> getMyReviews(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		List<ProductReview> productReviews = productReviewService.findMyReviews(userDetails.getMember());
		List<ReviewResponse> responses = productReviews.stream()
			.map(ReviewResponse::from)
			.toList();
		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@GetMapping("/{productId}")
	@Operation(summary = "상품 리뷰 조회 API", description = "해당 상품의 리뷰를 조회합니다")
	public ResponseEntity<Result<List<ReviewResponse>>> getReviewsByProduct(
		@PathVariable Long productId,
		@RequestParam(required = false) Integer star,
		@RequestParam(required = false, defaultValue = "LATEST") ReviewSort sort) {

		List<ProductReview> productReviews = productReviewService.findReviewsByProduct(productId, star, sort);
		List<ReviewResponse> responses = productReviews.stream()
			.map(ReviewResponse::from)
			.toList();
		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@PatchMapping("/{reviewId}")
	@Operation(summary = "리뷰 수정 API", description = "리뷰를 수정합니다.")
	public ResponseEntity<Void> updateReview(
		@PathVariable Long reviewId,
		@RequestBody UpdateReviewRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productReviewService.updateReview(reviewId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{reviewId}")
	@Operation(summary = "리뷰 삭제 API", description = "리뷰를 삭제합니다.")
	public ResponseEntity<Void> deleteReview(
		@PathVariable Long reviewId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productReviewService.deleteReview(reviewId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
