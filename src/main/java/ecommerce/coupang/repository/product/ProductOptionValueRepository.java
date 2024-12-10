package ecommerce.coupang.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.ProductOptionValue;

@LogLevel("ProductOptionValueRepository")
public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, Long> {
}
