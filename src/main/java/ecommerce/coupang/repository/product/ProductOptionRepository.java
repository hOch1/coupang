package ecommerce.coupang.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.ProductOption;

@LogLevel("ProductOptionRepository")
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
