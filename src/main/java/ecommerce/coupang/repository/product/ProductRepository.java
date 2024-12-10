package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.Product;

@LogLevel("ProductRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByStoreId(Long storeId);

	@Query("SELECT p FROM Product p WHERE p.category IN :categories")
	List<Product> findByProductInCategory(List<Category> categories);
}
