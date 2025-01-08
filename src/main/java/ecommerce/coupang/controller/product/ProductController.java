package ecommerce.coupang.controller.product;

import java.util.List;

import ecommerce.coupang.utils.member.MemberUtils;
import ecommerce.coupang.dto.request.PagingRequest;
import ecommerce.coupang.dto.request.product.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.service.product.query.ProductQueryService;
import ecommerce.coupang.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "상품 API V1", description = "상품 관련 API")
public class ProductController {

	private final ProductService productService;
	private final ProductQueryService productQueryService;

	@PostMapping("/{storeId}/store")
	@Operation(summary = "상품 등록 API", description = "상품을 등록합니다.")
	public ResponseEntity<Void> createProduct(
		@PathVariable Long storeId,
		@RequestBody @Valid CreateProductRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.createProduct(request, storeId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/search")
	@Operation(summary = "상품 조회 API", description = "상점, 카테고리, 옵션별로 상품을 조회합니다.")
	public ResponseEntity<Result<List<ProductResponse>>> searchProducts(
		@ModelAttribute ProductSearchRequest request,
		@ModelAttribute PagingRequest pagingRequest,
		@RequestParam(required = false, defaultValue = "LATEST") ProductSort sort,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		MemberGrade memberGrade = MemberUtils.getMemberGrade(userDetails);

		Page<ProductResponse> responses = productQueryService.search(request, sort, pagingRequest, memberGrade);
		return ResponseEntity.ok(new Result<>(
			responses.getContent(),
			responses.getContent().size(),
			responses.getNumber(),
			responses.getSize(),
			responses.getTotalPages(),
			responses.getTotalElements()
		));
	}

	@PatchMapping
	@Operation(summary = "상품 수정 API", description = "해당 상품을 수정합니다.")
	public ResponseEntity<Void> updateProduct(
		@RequestBody UpdateProductRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.updateProduct(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{productId}")
	@Operation(summary = "상품 삭제 API", description = "해당 상품을 Soft 하게 삭제 합니다")
	public ResponseEntity<Void> deleteProduct(
		@PathVariable Long productId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.deleteProduct(productId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
