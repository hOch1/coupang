package ecommerce.coupang.controller.product;

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

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.common.utils.MemberUtils;
import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductVariantRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
import ecommerce.coupang.service.product.ProductVariantService;
import ecommerce.coupang.service.product.query.ProductQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
@Tag(name = "상품 변형 API V1", description = "상품 변형 관련 API")
public class ProductVariantController {

	private final ProductVariantService productVariantService;
	private final ProductQueryService productQueryService;

	@PostMapping("/{productId}/product")
	@Operation(summary = "변형 상품 추가 API", description = "변형 상품을 추가합니다")
	public ResponseEntity<Void> createProductVariant(
		@PathVariable Long productId,
		@RequestBody @Valid CreateProductVariantRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productVariantService.addProductVariant(productId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{variantId}")
	@Operation(summary = "상품 상세 조회 API", description = "해당 상품을 상세 조회합니다.")
	public ResponseEntity<Result<ProductDetailResponse>> getProductById(
		@PathVariable Long variantId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		MemberGrade memberGrade = MemberUtils.getMemberGrade(userDetails);

		ProductDetailResponse response = productQueryService.findProduct(variantId, memberGrade);
		return ResponseEntity.ok(new Result<>(response));
	}


	@PatchMapping("/{variantId}/default")
	@Operation(summary = "대표상품 변경 API", description = "대표 상품을 변경합니다")
	public ResponseEntity<Void> updateDefaultProduct(
		@PathVariable Long variantId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productVariantService.updateDefaultProduct(variantId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{variantId}/stock")
	@Operation(summary = "상품 재고 변경 API", description = "상품의 재고를 변경 합니다")
	public ResponseEntity<Void> updateProductStock(
		@PathVariable Long variantId,
		@RequestBody UpdateProductStockRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productVariantService.updateProductStock(variantId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{variantId}/status")
	@Operation(summary = "상품 상태 변경 API", description = "상품의 상태를 변경 합니다")
	public ResponseEntity<Void> updateProductStatus(
		@PathVariable Long variantId,
		@RequestBody UpdateProductStatusRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productVariantService.updateProductStatus(variantId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{variantId}")
	@Operation(summary = "상품 변형 수정 API", description = "해당 변형 상품을 수정합니다")
	public ResponseEntity<Void> updateProductVariant(
		@PathVariable Long variantId,
		@RequestBody UpdateProductVariantRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productVariantService.updateProductVariant(variantId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{variantId}")
	@Operation(summary = "변형 상품 삭제 API", description = "해당 변형 상품을 Soft하게 삭제 합니다")
	public ResponseEntity<Void> deleteProductVariant(
		@PathVariable Long variantId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productVariantService.deleteProductVariant(variantId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
