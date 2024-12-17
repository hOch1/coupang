package ecommerce.coupang.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;

@LogLevel("CategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findByLevel(int level);
}
