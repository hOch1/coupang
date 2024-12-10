package ecommerce.coupang.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.dto.response.product.CategoryResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.CategoryRepository;
import ecommerce.coupang.service.product.CategoryService;
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
		Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
			new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

		if (!category.isBottom())
			throw new CustomException(ErrorCode.IS_NOT_BOTTOM_CATEGORY);

		return category;
	}

	@Override
	public List<CategoryResponse> findAll() {
		List<Category> categories = categoryRepository.findByLevel(1);
		List<CategoryResponse> responses = new ArrayList<>();

		for (Category category : categories)
			responses.add(CategoryResponse.from(category));

		return responses;
	}

	private void addAllSubCategory(Category category, List<Category> categories) {
		categories.add(category);
		for (Category children : category.getChildren())
			addAllSubCategory(children, categories);
	}
}
