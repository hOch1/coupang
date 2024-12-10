package ecommerce.coupang.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.CategoryOption;

@LogLevel("CategoryOptionRepository")
public interface CategoryOptionRepository extends JpaRepository<CategoryOption, Long> {

	List<CategoryOption> findByCategory(Category category);
}
