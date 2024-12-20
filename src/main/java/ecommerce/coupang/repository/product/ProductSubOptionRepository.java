package ecommerce.coupang.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.sub.ProductSubOption;

@LogLevel("ProductSubOptionRepository")
public interface ProductSubOptionRepository extends JpaRepository<ProductSubOption, Long> {
}
