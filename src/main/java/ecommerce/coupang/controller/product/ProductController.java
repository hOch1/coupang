package ecommerce.coupang.controller.product;

import java.util.List;

import org.springframework.data.domain.Page;
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

import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.ProductSort;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.UpdateProductVariantRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.product.ProductService;
import ecommerce.coupang.service.product.query.ProductQueryService;
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
		@RequestParam(required = false) Long categoryId,
		@RequestParam(required = false) Long storeId,
		@RequestParam(required = false) List<Long> categoryOptions,
		@RequestParam(required = false) List<Long> variantOptions,
		@RequestParam(required = false, defaultValue = "LATEST") ProductSort sort,
		@RequestParam(required = false, defaultValue = "0") int page,
		@RequestParam(required = false, defaultValue = "20") int pageSize) throws CustomException {

		Page<ProductResponse> responses = productQueryService.search(categoryId, storeId, categoryOptions, variantOptions, sort, page, pageSize);
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
		@PathVariable Long productVariantId) throws CustomException {

		ProductDetailResponse response = productQueryService.findProduct(productVariantId);
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
}
