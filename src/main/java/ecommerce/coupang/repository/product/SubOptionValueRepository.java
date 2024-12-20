package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.sub.CategorySubOption;
import ecommerce.coupang.domain.product.sub.SubOptionValue;

@LogLevel("SubOptionValueRepository")
public interface SubOptionValueRepository extends JpaRepository<SubOptionValue, Long> {
	List<SubOptionValue> findByCategorySubOption(CategorySubOption categorySubOption);
}
