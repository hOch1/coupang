package ecommerce.coupang.service.category;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.dto.response.option.AllOptionResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("CategoryService")
public interface CategoryService {

	/**
	 * 하위 카테고리 조회
	 * @param categoryId 카테고리 ID
	 * @return 조회한 카테고리
	 */
	@LogAction("하위 카테고리 조회")
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
	 * ID로 카테고리 조회 (Root Category 까지 fetch join)
	 * @param categoryId 카테고리 ID
	 * @return 조회한 카테고리
	 * @throws CustomException
	 */
	@LogAction("카테고리 조회 (최상위 부모 fetch join)")
	Category findCategoryWithRoot(Long categoryId) throws CustomException;

	/**
	 * 전체 카테고리 조회
	 * @return 전체 카테고리 목록
	 */
	@LogAction("전체 카테고리 조회")
	List<Category> findAll();

	/**
	 * 카테고리 옵션 조회
	 * @param categoryId 해당 카테고리 (최하위)
	 * @return 옵션 응답
	 */
	@LogAction("카테고리 옵션 조회")
	AllOptionResponse findOptions(Long categoryId) throws CustomException;
}
