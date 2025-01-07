package ecommerce.coupang.controller.product;

import java.util.List;

import ecommerce.coupang.dto.request.PagingRequest;
import ecommerce.coupang.dto.request.product.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.service.product.ProductQueryService;
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

	@PostMapping("/{productId}/variant")
	@Operation(summary = "변형 상품 추가 API", description = "변형 상품을 추가합니다")
	public ResponseEntity<Void> createProductVariant(
		@PathVariable Long productId,
		@RequestBody @Valid CreateProductVariantRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.addProductVariant(productId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/search")
	@Operation(summary = "상품 조회 API", description = "상점, 카테고리, 옵션별로 상품을 조회합니다.")
	public ResponseEntity<Result<List<ProductResponse>>> searchProducts(
		@ModelAttribute ProductSearchRequest request,
		@ModelAttribute PagingRequest pagingRequest,
		@RequestParam(required = false, defaultValue = "LATEST") ProductSort sort,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		MemberGrade memberGrade = getMemberGrade(userDetails);

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

	@GetMapping("/{productVariantId}")
	@Operation(summary = "상품 상세 조회 API", description = "해당 상품을 상세 조회합니다.")
	public ResponseEntity<Result<ProductDetailResponse>> getProductById(
		@PathVariable Long productVariantId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		MemberGrade memberGrade = getMemberGrade(userDetails);

		ProductDetailResponse response = productQueryService.findProduct(productVariantId, memberGrade);
		return ResponseEntity.ok(new Result<>(response));
	}

	@PatchMapping("/{productVariantId}/default")
	@Operation(summary = "대표상품 변경 API", description = "대표 상품을 변경합니다")
	public ResponseEntity<Void> updateDefaultProduct(
		@PathVariable Long productVariantId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.updateDefaultProduct(productVariantId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{productVariantId}/stock")
	@Operation(summary = "상품 재고 변경 API", description = "상품의 재고를 변경 합니다")
	public ResponseEntity<Void> updateProductStock(
		@PathVariable Long productVariantId,
		@RequestBody UpdateProductStockRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.updateProductStock(productVariantId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{productVariantId}/status")
	@Operation(summary = "상품 상태 변경 API", description = "상품의 상태를 변경 합니다")
	public ResponseEntity<Void> updateProductStatus(
		@PathVariable Long productVariantId,
		@RequestBody UpdateProductStatusRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.updateProductStatus(productVariantId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping
	@Operation(summary = "상품 수정 API", description = "해당 상품을 수정합니다.")
	public ResponseEntity<Void> updateProduct(
		@RequestBody UpdateProductRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.updateProduct(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/variant")
	@Operation(summary = "상품 변형 수정 API", description = "해당 변형 상품을 수정합니다")
	public ResponseEntity<Void> updateProductVariant(
		@RequestBody UpdateProductVariantRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.updateProductVariant(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{productId}")
	@Operation(summary = "상품 삭제 API", description = "해당 상품을 Soft하게 삭제 합니다")
	public ResponseEntity<Void> deleteProduct(
		@PathVariable Long productId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.deleteProduct(productId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/variant/{productVariantId}")
	@Operation(summary = "변형 상품 삭제 API", description = "해당 변형 상품을 Soft하게 삭제 합니다")
	public ResponseEntity<Void> deleteProductVariant(
		@PathVariable Long productVariantId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.deleteProductVariant(productVariantId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	private static MemberGrade getMemberGrade(CustomUserDetails userDetails) {
		return (userDetails == null || userDetails.getMember() == null)
				? MemberGrade.NORMAL
				: userDetails.getMember().getGrade();
	}

}
