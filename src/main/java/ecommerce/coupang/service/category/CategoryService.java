package ecommerce.coupang.service.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.dto.response.option.AllOptionResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@LogLevel("CategoryService")
public class CategoryService {

	private final CategoryRepository categoryRepository;

	/**
	 * 하위 카테고리 조회
	 * @param categoryId 카테고리 ID
	 * @return 조회한 카테고리
	 */
	@LogAction("하위 카테고리 조회")
	public List<Category> findAllSubCategories(Long categoryId) {
		return categoryRepository.findAllByChildren(categoryId);
	}

	/**
	 * ID로 카테고리 조회 (최하위 카테고리 체크)
	 * @param categoryId 카테고리 ID
	 * @return 조회한 카테고리
	 * @throws CustomException
	 */
	@LogAction("카테고리 조회 (최하위 체크)")
	public Category findBottomCategory(Long categoryId) throws CustomException {
		Category category = categoryRepository.findByIdWithParent(categoryId).orElseThrow(() ->
			new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

		if (!category.isBottom())
			throw new CustomException(ErrorCode.IS_NOT_BOTTOM_CATEGORY);

		return category;
	}

	/**
	 * ID로 카테고리 조회 (Root Category 까지 fetch join)
	 * @param categoryId 카테고리 ID
	 * @return 조회한 카테고리
	 * @throws CustomException
	 */
	@LogAction("카테고리 조회 (최상위 부모 fetch join)")
	public Category findCategoryWithRoot(Long categoryId) throws CustomException {
		return categoryRepository.findCategoryWithRoot(categoryId)
			.orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	/**
	 * 전체 카테고리 조회
	 * @return 전체 카테고리 목록
	 */
	@LogAction("전체 카테고리 조회")
	public List<Category> findAll() {
		return categoryRepository.findByLevel(1);
	}

	/**
	 * 카테고리 옵션 조회
	 * @param categoryId 해당 카테고리 (최하위)
	 * @return 옵션 응답
	 */
	@LogAction("카테고리 옵션 조회")
	public AllOptionResponse findOptions(Long categoryId) throws CustomException {
		Category category = findCategoryWithRoot(categoryId);

		List<AllOptionResponse.CategoryOptionResponse> categoryOptions = new ArrayList<>();
		List<AllOptionResponse.VariantOptionResponse> variantOptions = new ArrayList<>();

		while (category != null) {
			categoryOptions.addAll(
				category.getCategoryOptions().stream()
					.map(AllOptionResponse.CategoryOptionResponse::from)
					.toList()
			);
			variantOptions.addAll(
				category.getVariantOptions().stream()
					.map(AllOptionResponse.VariantOptionResponse::from)
					.toList()
			);
			category = category.getParent();
		}

		return new AllOptionResponse(categoryOptions, variantOptions);
	}
}
