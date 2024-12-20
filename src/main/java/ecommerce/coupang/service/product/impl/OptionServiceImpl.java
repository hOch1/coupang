package ecommerce.coupang.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.sub.CategorySubOption;
import ecommerce.coupang.domain.product.sub.SubOptionValue;
import ecommerce.coupang.dto.response.product.OptionResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.category.CategorySubOptionRepository;
import ecommerce.coupang.repository.product.SubOptionValueRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

	private final CategoryService categoryService;
	private final CategorySubOptionRepository categorySubOptionRepository;
	private final SubOptionValueRepository subOptionValueRepository;

	@Override
	public List<OptionResponse> findCategoryOption(Long categoryId) throws CustomException {
		List<OptionResponse> responses = new ArrayList<>();
		Category category = categoryService.findBottomCategory(categoryId);

		while (category != null) {
			List<CategorySubOption> categorySubOptions = categorySubOptionRepository.findByCategory(category);

			for (CategorySubOption categorySubOption : categorySubOptions) {
				List<SubOptionValue> subOptionValues = subOptionValueRepository.findByCategorySubOption(
					categorySubOption);
				responses.add(OptionResponse.from(categorySubOption, subOptionValues));
			}

			category = category.getParent();
		}

		return responses;
	}
}
