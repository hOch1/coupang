package ecommerce.coupang.repository.product;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

@LogLevel("VariantOptionValueRepository")
public interface VariantOptionValueRepository extends JpaRepository<VariantOptionValue, Long> {
}
