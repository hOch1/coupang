package ecommerce.coupang.repository.category;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.category.CategoryOption;

@LogLevel("CategoryOptionValueRepository")
public interface CategoryOptionValueRepository extends JpaRepository<CategoryOptionValue, Long> {

}
