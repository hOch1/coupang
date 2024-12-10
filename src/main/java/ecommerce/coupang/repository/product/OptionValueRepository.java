package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.domain.product.OptionValue;

@LogLevel("OptionValueRepository")
public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {
	List<OptionValue> findByCategoryOption(CategoryOption categoryOption);
}
