package ecommerce.coupang.repository.product.custom;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.variant.ProductVariant;

import java.util.List;

public interface ProductCustomRepository {

	/**
	 * 옵션별 상품 조회 (대표상품)
	 * @param categories 포함된 카테고리
	 * @param storeId 상점 ID
	 * @param categoryOptions 포함된 카테고리 옵션
	 * @param variantOptions 포함된 변형 옵션
	 * @return 상품 변형 목록 (대표상품)
	 */
	List<ProductVariant> searchProducts(List<Category> categories, Long storeId, List<Long> categoryOptions, List<Long> variantOptions);
}
