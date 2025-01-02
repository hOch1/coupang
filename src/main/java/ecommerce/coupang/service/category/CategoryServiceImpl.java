package ecommerce.coupang.service.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.dto.response.option.AllOptionResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public List<Category> findAllSubCategories(Long categoryId) throws CustomException {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
			new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
		List<Category> categories = new ArrayList<>();

		addAllSubCategory(category, categories);

		return categories;
	}

	@Override
	public Category findBottomCategory(Long categoryId) throws CustomException {
		Category category = categoryRepository.findByIdWithParent(categoryId).orElseThrow(() ->
			new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

		if (!category.isBottom())
			throw new CustomException(ErrorCode.IS_NOT_BOTTOM_CATEGORY);

		return category;
	}

	@Override
	public Category findCategoryWithRoot(Long categoryId) throws CustomException {
		return categoryRepository.findCategoryWithRoot(categoryId)
			.orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	@Override
	public List<Category> findAll() {
		return categoryRepository.findByLevel(1);
	}

	@Override
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

	private void addAllSubCategory(Category category, List<Category> categories) {
		categories.add(category);
		for (Category children : category.getChildren())
			addAllSubCategory(children, categories);
	}
}
