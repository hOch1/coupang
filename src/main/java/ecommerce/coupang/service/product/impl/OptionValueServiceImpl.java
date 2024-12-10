package ecommerce.coupang.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.ProductOptionValue;
import ecommerce.coupang.dto.response.product.OptionResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.CategoryOptionRepository;
import ecommerce.coupang.repository.product.OptionValueRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionValueService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionValueServiceImpl implements OptionValueService {

	private final CategoryService categoryService;
	private final OptionValueRepository optionValueRepository;
	private final CategoryOptionRepository categoryOptionRepository;

	@Override
	public ProductOptionValue createProductOptionValue(Long optionId) throws CustomException {
		OptionValue optionValue = optionValueRepository.findById(optionId).orElseThrow(() ->
			new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

		return ProductOptionValue.create(optionValue);
	}

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

	@Override
	public OptionValue findOptionValue(Long optionId) throws CustomException {
		return optionValueRepository.findById(optionId).orElseThrow(() ->
			new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));
	}
}
