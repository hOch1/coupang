package ecommerce.coupang.service.product.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.dto.request.product.ProductSort;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductCategoryOptionRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.repository.store.CouponProductRepository;
import ecommerce.coupang.service.category.CategoryService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

	private final ProductRepository productRepository;
	private final ProductVariantRepository productVariantRepository;
	private final ProductCategoryOptionRepository productCategoryOptionRepository;
	private final ProductVariantOptionRepository productVariantOptionRepository;
	private final CouponProductRepository couponProductRepository;
	private final CategoryService categoryService;

	/**
	 * 상품 상세 조회
	 * @param productVariantId 조회할 상품 ID
	 * @return 상품 상세 조회정보
	 */
	@LogAction("상품 상세 조회")
	public ProductDetailResponse findProduct(Long productVariantId) throws CustomException {
		ProductVariant productVariant = productVariantRepository.findByIdWithStoreAndCategory(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		List<ProductCategoryOption> productCategoryOptions = productCategoryOptionRepository.findByProductId(productVariant.getProduct().getId());
		List<ProductVariantOption> productVariantOptions = productVariantOptionRepository.findByProductVariantId(productVariant.getId());
		List<CouponProduct> couponProducts = couponProductRepository.findByProductId(productVariant.getProduct().getId());
		Category category = categoryService.findCategoryWithRoot(productVariant.getProduct().getCategory().getId());

		return ProductDetailResponse.from(productVariant, category, productCategoryOptions, productVariantOptions, couponProducts);
	}

	/**
	 * 옵션별 상품 조회
	 * @param categoryId 카테고리 ID
	 * @param storeId 상점 ID
	 * @param categoryOptions 카테고리 옵션 값 ID
	 * @param variantOptions 변형 옵션 값 ID
	 * @return 조회 상품 리스트
	 * @throws CustomException
	 */
	@LogAction("상품 목록 조회")
	public Page<ProductVariant> search(Long categoryId, Long storeId, List<Long> categoryOptions, List<Long> variantOptions, ProductSort sort, int page, int pageSize) throws CustomException{
		List<Category> categories = new ArrayList<>();

		if (categoryId != null)
			categories = categoryService.findAllSubCategories(categoryId);

		return productRepository.searchProducts(categories, storeId, categoryOptions, variantOptions, sort, PageRequest.of(page, pageSize));
	}

}
