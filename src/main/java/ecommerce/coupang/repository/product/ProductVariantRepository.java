package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.category c "
		+ "join fetch p.store s "
		+ "where p in :products "
		+ "and pv.isDefault = true "
		+ "order by p.createdAt desc ")
	List<ProductVariant> findByProducts(List<Product> products);

	@Query("select pv from ProductVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.category c "
		+ "join fetch p.store s "
		+ "where pv.product = :product "
		+ "order by p.createdAt desc ")
	List<ProductVariant> findByProduct(Product product);
}
