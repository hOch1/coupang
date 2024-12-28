package ecommerce.coupang.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.product.ProductInquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product/inquiries")
@RequiredArgsConstructor
@Tag(name = "상품 문의 API", description = "상품 문의관련 API")
public class ProductInquiryController {

	private final ProductInquiryService productInquiryService;

	@PostMapping("/{productId}")
	@Operation(summary = "상품 문의 등록 API", description = "상품 문의를 등록합니다")
	public ResponseEntity<Void> createProductInquiry(
		@PathVariable Long productId,
		@RequestBody CreateInquiryRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productInquiryService.createInquiry(productId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/{inquiryId}/answer")
	@Operation(summary = "상품 문의의 답변 등록 API", description = "상품 문의에 대한 답변을 등록합니다.")
	public ResponseEntity<Void> createAnswer(
		@PathVariable Long inquiryId,
		@RequestBody CreateAnswerRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/me")
	@Operation(summary = "내가 등록한 문의 조회 API", description = "내가 등록한 상품 문의 목록을 조회합니다.")
	public ResponseEntity<Result<Void>> findMyInquiries(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		return ResponseEntity.ok(null);
	}

}
