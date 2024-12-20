package ecommerce.coupang.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.ProductCategoryOption;

@LogLevel("ProductCategoryOptionRepository")
public interface ProductCategoryOptionRepository extends JpaRepository<ProductCategoryOption, Long> {
}
