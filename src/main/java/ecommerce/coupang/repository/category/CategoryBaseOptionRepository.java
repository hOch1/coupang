package ecommerce.coupang.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.CategoryBaseOption;

@LogLevel("CategoryBaseOptionRepository")
public interface CategoryBaseOptionRepository extends JpaRepository<CategoryBaseOption, Long> {

	List<CategoryBaseOption> findByCategory(Category category);
}
