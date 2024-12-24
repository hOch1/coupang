package ecommerce.coupang.repository.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.category.Category;

@LogLevel("CategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {

	/**
	 * 카테고리 레벨로 조회
	 * @param level 카테고리 레벨
	 * @return level 별 카테고리 목록
	 */
	List<Category> findByLevel(int level);
}
