package ecommerce.coupang.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.category.Category;

@LogLevel("CategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findByLevel(int level);
}
