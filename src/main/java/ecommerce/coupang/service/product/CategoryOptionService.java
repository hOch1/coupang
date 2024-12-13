package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.CategoryOption;

@LogLevel("CategoryOptionService")
public interface CategoryOptionService {

	/**
	 * 카테고리 옵션 조회
	 * @param category 찾을 카테고리
	 * @return 카테고리 옵션 목록
	 */
	@LogAction("카테고리 옵션 조회")
	List<CategoryOption> findByCategory(Category category);
}
