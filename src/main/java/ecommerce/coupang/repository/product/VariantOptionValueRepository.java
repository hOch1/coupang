package ecommerce.coupang.repository.product;

import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantOptionValueRepository extends JpaRepository<VariantOptionValue, Long> {
}
