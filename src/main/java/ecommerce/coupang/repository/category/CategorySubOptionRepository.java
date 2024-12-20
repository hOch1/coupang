package ecommerce.coupang.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.sub.CategorySubOption;

@LogLevel("CategorySubOptionRepository")
public interface CategorySubOptionRepository extends JpaRepository<CategorySubOption, Long> {

	List<CategorySubOption> findByCategory(Category category);
}
