package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.product.ProductCategoryOption;

public interface ProductCategoryOptionRepository extends JpaRepository<ProductCategoryOption, Long> {

	@Query("select pco from ProductCategoryOption pco "
		+ "join fetch pco.categoryOptionValue ov "
		+ "join fetch ov.categoryOption co "
		+ "where pco.product.id = :productId")
	List<ProductCategoryOption> findByProductId(Long productId);
}