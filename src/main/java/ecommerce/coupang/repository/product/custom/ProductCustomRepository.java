package ecommerce.coupang.repository.product.custom;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.ProductSort;
import ecommerce.coupang.dto.response.product.ProductResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {

	/**
	 * 옵션별 상품 조회 (대표상품)
	 *
	 * @param categories      포함된 카테고리
	 * @param storeId         상점 ID
	 * @param categoryOptions 포함된 카테고리 옵션
	 * @param variantOptions  포함된 변형 옵션
	 * @return 상품 변형 목록 (대표상품)
	 */
	Page<ProductResponse> searchProducts(
		List<Category> categories,
		Long storeId,
		List<Long> categoryOptions,
		List<Long> variantOptions,
		ProductSort sort,
		Pageable pageable);

	/**
	 * ID로 상품 조회
	 * 기본 정렬 - 최신 등록순
	 * @param productId 상품 ID
	 * @return 상품
	 */
	Optional<Product> findByIdWithMemberAndCategory(Long productId);
}
