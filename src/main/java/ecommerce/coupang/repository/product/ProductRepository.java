package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.Product;

@LogLevel("ProductRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * 상점으로 상품 조회
	 * 기본 정렬 - 최신 등록순
	 * @param storeId 상점 ID
	 * @return 상품 리스트
	 */
	@Query("SELECT p FROM Product p "
		+ "WHERE p.store.id = :storeId "
		+ "ORDER BY p.createdAt DESC")
	List<Product> findByStore(Long storeId);

	/**
	 * 카테고리별 상품 조회
	 * 기본 정렬 - 최신 등록순
	 * @param categories 포함된 카테고리
	 * @return 상품 리스트
	 */
	@Query("SELECT p FROM Product p "
		+ "WHERE p.category IN :categories "
		+ "ORDER BY p.createdAt DESC")
	List<Product> findByCategories(List<Category> categories);

	/**
	 * 카테고리 + 상점별 상품 조회
	 * @param storeId 상점 ID
	 * @param categories 포함된 카테고리
	 * @return 상품 리스트
	 */
	@Query("SELECT p FROM Product p "
		+ "WHERE p.category IN :categories "
		+ "AND p.store.id = :storeId "
		+ "ORDER BY p.createdAt DESC")
	List<Product> findByCategoriesAndStore(Long storeId, List<Category> categories);

	/**
	 * 카테고리 + 옵션별 상품 조회
	 * @param categories 포함된 카테고리
	 * @param options 포함된 옵션
	 * @return 상품 리스트
	 */
	@Query("SELECT p FROM Product p "
		+ "JOIN p.productDetails pd "
		+ "JOIN pd.productOptions pov "
		+ "WHERE p.category IN :categories "
		+ "AND pov.optionValue.id IN :options "
		+ "ORDER BY p.createdAt DESC")
	List<Product> findByCategoriesAndOptions(List<Category> categories, List<Long> options);

	/**
	 * 상점 + 옵션별 상품 조회
	 * @param storeId 상점 ID
	 * @param options 포함된 옵션
	 * @return 상품 리스트
	 */
	@Query("SELECT p FROM Product p "
		+ "JOIN p.productDetails pd "
		+ "JOIN pd.productOptions pov "
		+ "WHERE p.store.id = :storeId "
		+ "AND pov.optionValue.id IN :options "
		+ "ORDER BY p.createdAt DESC")
	List<Product> findByStoreAndOptions(Long storeId, List<Long> options);

	/**
	 * 상점 + 카테고리 + 옵션별 상품 조회
	 * @param storeId 상점 ID
	 * @param categories 포함된 카테고리
	 * @param options 포함된 옵션
	 * @return 상품 리스트
	 */
	@Query("SELECT p FROM Product p "
		+ "JOIN p.productDetails pd "
		+ "JOIN pd.productOptions pov "
		+ "WHERE p.category IN :categories "
		+ "AND p.store.id = :storeId "
		+ "AND pov.optionValue.id IN :options "
		+ "ORDER BY p.createdAt DESC")
	List<Product> findByStoreAndCategoryAndOptions(Long storeId, List<Category> categories, List<Long> options);
}
