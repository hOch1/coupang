package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.dto.response.product.CategoryResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("CategoryService")
public interface CategoryService {

	/**
	 * 하위 카테고리 조회
	 * @param categoryId 카테고리 ID
	 * @return 조회한 카테고리
	 */
	@LogAction("카테고리 조회")
	List<Category> findAllSubCategories(Long categoryId) throws CustomException;

	/**
	 * ID로 카테고리 조회 (최하위 카테고리 체크)
	 * @param categoryId 카테고리 ID
	 * @return 조회한 카테고리
	 * @throws CustomException
	 */
	@LogAction("카테고리 조회 (최하위 체크)")
	Category findBottomCategory(Long categoryId) throws CustomException;

	/**
	 * 전체 카테고리 조회
	 * @return 전체 카테고리 목록
	 */
	@LogAction("전체 카테고리 조회")
	List<CategoryResponse> findAll();
}
