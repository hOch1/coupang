package ecommerce.coupang.controller.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.UpdateProductVariantRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.product.ProductCategoryOptionRepository;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "상품 API V1", description = "상품 관련 API")
public class ProductController {

	private final ProductService productService;
	private final ProductVariantOptionRepository productVariantOptionRepository;
	private final ProductCategoryOptionRepository productCategoryOptionRepository;

	@PostMapping
	@Operation(summary = "상품 등록 API", description = "상품을 등록합니다.")
	public ResponseEntity<Void> createProduct(
		@RequestBody CreateProductRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		productService.createProduct(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/category/{categoryId}")
	@Operation(summary = "카테고리별 상품 조회 API", description = "해당 카테고리 하위 상품들을 조회합니다.")
	public ResponseEntity<Result<List<ProductResponse>>> getProductsByCategory(
		@PathVariable Long categoryId) throws CustomException {

		List<ProductVariant> productVariants = productService.findProductsByCategory(categoryId);
		List<ProductResponse> responses = productVariants.stream()
			.map(ProductResponse::from)
			.toList();

		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@GetMapping("/category/{categoryId}/options")
	@Operation(summary = "카테고리 + 옵션별 상품 조회 API", description = "카테고리별 상품 조회 API + 옵션 추가")
	public ResponseEntity<List<ProductResponse>> getProductsByCategoryAndOptions(
		@PathVariable Long categoryId,
		@RequestParam List<Long> categoryOptions,
		@RequestParam List<Long> variantOptions) throws CustomException {

		List<Product> products = productService.findProductsByCategoryAndOptions(categoryId, categoryOptions, variantOptions);
		// return ResponseEntity.ok(products.stream()
		// 	.map(ProductResponse::from)
		// 	.toList());
		return null;
	}

	@GetMapping("/store/{storeId}")
	@Operation(summary = "상점별 상품 조회 API", description = "해당 상점 상품들을 조회합니다.")
	public ResponseEntity<Result<List<ProductResponse>>> getProductsByStore(
		@PathVariable Long storeId) throws CustomException {

		List<ProductVariant> productVariants = productService.findProductsByStore(storeId);
		List<ProductResponse> responses = productVariants.stream()
			.map(ProductResponse::from)
			.toList();

		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@GetMapping("/store/{storeId}/options")
	@Operation(summary = "상점 + 옵션별 상품 조회 API", description = "상점별 상품 조회 API + 옵션")
	public ResponseEntity<List<ProductResponse>> getProductsByStoreAndOptions(
		@PathVariable Long storeId,
		@RequestParam List<Long> categoryOptions,
		@RequestParam List<Long> variantOptions) throws CustomException {

		List<Product> products = productService.findProductsByStoreAndOptions(storeId, categoryOptions, variantOptions);
		// return ResponseEntity.ok(products.stream()
		// 	.map(ProductResponse::from)
		// 	.toList());
		return null;
	}

	@GetMapping("/store/{storeId}/category/{categoryId}")
	@Operation(summary = "상점 + 카테고리별 상품 조회 API", description = "해당 상점과 하위 카테고리별 상품을 조회합니다.")
	public ResponseEntity<List<ProductResponse>> getProductsByStoreAndCategory(
		@PathVariable("storeId") Long storeId,
		@PathVariable("categoryId") Long categoryId) throws CustomException {

		List<ProductVariant> productVariants = productService.findProductsByStoreAndCategory(storeId, categoryId);
		return ResponseEntity.ok(productVariants.stream()
			.map(ProductResponse::from)
			.toList());
	}

	@GetMapping("/store/{storeId}/category/{categoryId}/options")
	@Operation(summary = "상점 + 카테고리 + 옵션별 상품 조회 API", description = "상점 + 카테고리별 상품 조회 API + 옵션")
	public ResponseEntity<List<ProductResponse>> getProductsByStoreAndCategoryAndOptions(
		@PathVariable("storeId") Long storeId,
		@PathVariable("categoryId") Long categoryId,
		@RequestParam List<Long> categoryOptions,
		@RequestParam List<Long> variantOptions) throws CustomException {

		List<Product> products = productService.findProductsByStoreAndCategoryAndOptions(storeId, categoryId, categoryOptions, variantOptions);
		// return ResponseEntity.ok(products.stream()
		// 	.map(ProductResponse::from)
		// 	.toList());
		return null;
	}

	@GetMapping("/{productVariantId}")
	@Operation(summary = "상품 상세 조회 API", description = "해당 상품을 상세 조회합니다.")
	public ResponseEntity<Result<ProductDetailResponse>> getProductById(
		@PathVariable Long productVariantId) throws CustomException {

		ProductVariant productVariant = productService.findProduct(productVariantId);
		List<ProductCategoryOption> productCategoryOptions = productCategoryOptionRepository.findByProductId(productVariant.getProduct().getId());
		List<ProductVariantOption> productVariantOptions = productVariantOptionRepository.findByProductVariantId(productVariantId);

		return ResponseEntity.ok(new Result<>(ProductDetailResponse.from(productVariant, productCategoryOptions, productVariantOptions)));
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
}
