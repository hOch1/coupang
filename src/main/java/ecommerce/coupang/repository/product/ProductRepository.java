package ecommerce.coupang.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;

@LogLevel("ProductRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where p.id = :productId "
		+ "order by p.createdAt desc ")
	Optional<Product> findById(Long productId);

	/**
	 * 상점으로 상품 조회
	 * 기본 정렬 - 최신 등록순
	 * @param storeId 상점 ID
	 * @return 상품 리스트
	 */
	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where p.store.id = :storeId "
		+ "order by p.createdAt desc ")
	List<Product> findByStore(Long storeId);

	/**
	 * 카테고리별 상품 조회
	 * 기본 정렬 - 최신 등록순
	 * @param categories 포함된 카테고리
	 * @return 상품 리스트
	 */
	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where p.category in :categories "
		+ "order by p.createdAt desc ")
	List<Product> findByCategories(List<Category> categories);

	/**
	 * 카테고리 + 상점별 상품 조회
	 * @param storeId 상점 ID
	 * @param categories 포함된 카테고리
	 * @return 상품 리스트
	 */
	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where p.category in :categories "
		+ "and p.store.id = :storeId "
		+ "order by p.createdAt desc ")
	List<Product> findByCategoriesAndStore(Long storeId, List<Category> categories);

	/**
	 * 카테고리 + 옵션별 상품 조회
	 * @param categories 포함된 카테고리
	 * @param options 포함된 옵션
	 * @return 상품 리스트
	 */
	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where p.category in :categories "
		+ "order by p.createdAt desc ")
	List<Product> findByCategoriesAndOptions(List<Category> categories, List<Long> options);

	/**
	 * 상점 + 옵션별 상품 조회
	 * @param storeId 상점 ID
	 * @param options 포함된 옵션
	 * @return 상품 리스트
	 */
	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where p.store.id = :storeId "
		+ "order by p.createdAt desc ")
	List<Product> findByStoreAndOptions(Long storeId, List<Long> options);

	/**
	 * 상점 + 카테고리 + 옵션별 상품 조회
	 * @param storeId 상점 ID
	 * @param categories 포함된 카테고리
	 * @param options 포함된 옵션
	 * @return 상품 리스트
	 */
	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where p.category in :categories "
		+ "and p.store.id = :storeId "
		+ "order by p.createdAt desc ")
	List<Product> findByStoreAndCategoryAndOptions(Long storeId, List<Category> categories, List<Long> options);
}
