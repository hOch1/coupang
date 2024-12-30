package ecommerce.coupang.repository.product;

import java.util.Optional;

import ecommerce.coupang.repository.product.custom.ProductCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Product;

@LogLevel("ProductRepository")
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

	/**
	 * ID로 상품 조회
	 * 기본 정렬 - 최신 등록순
	 * @param productId must not be {@literal null}.
	 * @return 상품
	 */
	@Query("select p from Product p "
		+ "join fetch p.store s "
		+ "join fetch s.member m "
		+ "join fetch p.category c "
		+ "where p.id = :productId "
		+ "and s.isActive = true "
		+ "and p.isActive = true "
		+ "order by p.createdAt desc ")
	Optional<Product> findByIdWithMemberAndCategory(Long productId);
}
