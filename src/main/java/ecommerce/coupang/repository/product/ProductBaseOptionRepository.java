package ecommerce.coupang.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.ProductBaseOption;

@LogLevel("ProductBaseOptionRepository")
public interface ProductBaseOptionRepository extends JpaRepository<ProductBaseOption, Long> {
}
