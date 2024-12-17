package ecommerce.coupang.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.dto.response.product.OptionResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.product.CategoryOptionRepository;
import ecommerce.coupang.repository.product.OptionValueRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

	private final CategoryService categoryService;
	private final CategoryOptionRepository categoryOptionRepository;
	private final OptionValueRepository optionValueRepository;

	@Override
	public List<OptionResponse> findCategoryOption(Long categoryId) throws CustomException {
		List<OptionResponse> responses = new ArrayList<>();
		Category category = categoryService.findBottomCategory(categoryId);

		while (category != null) {
			List<CategoryOption> categoryOptions = categoryOptionRepository.findByCategory(category);

			for (CategoryOption categoryOption : categoryOptions) {
				List<OptionValue> optionValues = optionValueRepository.findByCategoryOption(categoryOption);
				responses.add(OptionResponse.from(categoryOption, optionValues));
			}

			category = category.getParent();
		}

		return responses;
	}
}
