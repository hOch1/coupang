package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.product.variant.ProductVariantOption;

public interface ProductVariantOptionRepository extends JpaRepository<ProductVariantOption, Long> {

	@Query("select pvo from ProductVariantOption pvo "
		+ "join fetch pvo.variantOptionValue vov "
		+ "join fetch vov.variantOption vo "
		+ "where pvo.productVariant.id = :productVariantId ")
	List<ProductVariantOption> findByProductVariantId(Long productVariantId);
}
