package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.BaseOptionValue;
import ecommerce.coupang.domain.product.CategoryBaseOption;

@LogLevel("BaseOptionValueRepository")
public interface BaseOptionValueRepository extends JpaRepository<BaseOptionValue, Long> {
	List<BaseOptionValue> findByCategoryBaseOption(CategoryBaseOption categoryBaseOption);
}
