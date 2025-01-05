package ecommerce.coupang.repository.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.product.variant.ProductVariant;

@LogLevel("ProductVariantRepository")
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where pv.id = :productVariantId "
		+ "and s.isActive = true "
		+ "and p.isActive = true ")
	Optional<ProductVariant> findByIdWithStoreAndCategory(Long productVariantId);

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "where pv.id = :productVariantId "
		+ "and s.isActive = true "
		+ "and p.isActive = true ")
	Optional<ProductVariant> findByIdWithStore(Long productVariantId);

	@Query("select pv from ProductVariant pv "
		+ "join pv.product p "
		+ "where p.id = :productId "
		+ "and pv.isDefault = true ")
	Optional<ProductVariant> findByProductIdAndDefault(Long productId);

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "join fetch s.member m "
		+ "where pv.id = :productVariantId "
		+ "and s.isActive = true "
		+ "and p.isActive = true ")
	Optional<ProductVariant> findByIdWithMember(Long productVariantId);
}
