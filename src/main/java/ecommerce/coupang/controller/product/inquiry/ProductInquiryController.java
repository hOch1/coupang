package ecommerce.coupang.controller.product.inquiry;

import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.dto.request.product.inquiry.UpdateInquiryRequest;
import ecommerce.coupang.dto.response.product.inquiry.InquiryResponse;
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
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.service.inqury.ProductInquiryService;
import ecommerce.coupang.service.inqury.query.ProductInquiryQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product/inquiries")
@RequiredArgsConstructor
@Tag(name = "상품 문의 API V1", description = "상품 문의, 답변관련 API")
public class ProductInquiryController {

	private final ProductInquiryService productInquiryService;
	private final ProductInquiryQueryService productInquiryQueryService;

	@PostMapping("/{productId}")
	@Operation(summary = "상품 문의 등록 API", description = "상품 문의를 등록합니다")
	public ResponseEntity<Void> createProductInquiry(
		@PathVariable Long productId,
		@RequestBody @Valid CreateInquiryRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productInquiryService.createInquiry(productId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/me")
	@Operation(summary = "내가 등록한 문의 조회 API", description = "내가 등록한 상품 문의 목록을 조회합니다.")
	public ResponseEntity<Result<List<InquiryResponse>>> findMyInquiries(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		List<ProductInquiry> productInquiries = productInquiryQueryService.findMyInquiries(userDetails.getMember());
		List<InquiryResponse> responses = productInquiries.stream()
				.map(InquiryResponse::from)
				.toList();
		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@GetMapping("/{productId}/product")
	@Operation(summary = "상품 문의 조회 API", description = "해당 상품의 문의 내용을 조회합니다")
	public ResponseEntity<Result<List<InquiryResponse>>> findInquiriesByProduct(
		@PathVariable Long productId) throws CustomException {

		List<ProductInquiry> productInquiries = productInquiryQueryService.getInquiryByProduct(productId);
		List<InquiryResponse> responses = productInquiries.stream()
				.map(InquiryResponse::from)
				.toList();
		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}


	@PatchMapping("/{inquiryId}")
	@Operation(summary = "문의 수정 API", description = "해당 문의를 수정합니다")
	public ResponseEntity<Void> updateInquiry(
		@PathVariable Long inquiryId,
		@RequestBody UpdateInquiryRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productInquiryService.updateInquiry(inquiryId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}


	@DeleteMapping("/{inquiryId}")
	@Operation(summary = "문의 삭제 API", description = "해당 문의를 삭제합니다")
	public ResponseEntity<Void> deleteInquiry(
		@PathVariable Long inquiryId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productInquiryService.deleteInquiry(inquiryId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
