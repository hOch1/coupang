package ecommerce.coupang.repository.product;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.category.VariantOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

@LogLevel("VariantOptionValueRepository")
public interface VariantOptionValueRepository extends JpaRepository<VariantOptionValue, Long> {
}
