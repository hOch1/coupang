package ecommerce.coupang.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.category.CategoryOptionValue;

@LogLevel("CategoryOptionValueRepository")
public interface CategoryOptionValueRepository extends JpaRepository<CategoryOptionValue, Long> {

}
