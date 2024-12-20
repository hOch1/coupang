package ecommerce.coupang.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.CategoryOption;

@LogLevel("CategoryOptionRepository")
public interface CategoryOptionRepository extends JpaRepository<CategoryOption, Long> {

	List<CategoryOption> findByCategory(Category category);
}
