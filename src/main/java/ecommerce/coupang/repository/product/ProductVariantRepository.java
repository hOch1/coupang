package ecommerce.coupang.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "join fetch p.category c "
		+ "where pv.id = :productVariantId")
	Optional<ProductVariant> findByIdWithStoreAndCategory(Long productVariantId);

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "where pv.id = :productVariantId ")
	Optional<ProductVariant> findByIdWithStore(Long productVariantId);

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.category c "
		+ "join fetch p.store s "
		+ "where p in :products "
		+ "and pv.isDefault = true "
		+ "order by p.createdAt desc ")
	List<ProductVariant> findByProducts(List<Product> products);

	@Query("select pv from ProductVariant pv "
		+ "join pv.product p "
		+ "where p.id = :productId "
		+ "and pv.isDefault = true ")
	Optional<ProductVariant> findByProductIdAndDefault(Long productId);

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "join fetch s.member m "
		+ "where pv.id = :productVariantId ")
	Optional<ProductVariant> findByIdWithMember(Long productVariantId);
}
