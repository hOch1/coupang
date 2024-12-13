package ecommerce.coupang.service.product.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.OptionValueRepository;
import ecommerce.coupang.service.product.OptionValueService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionValueServiceImpl implements OptionValueService {

	private final OptionValueRepository optionValueRepository;

	@Override
	public OptionValue findOptionValue(Long id) throws CustomException {
		return optionValueRepository.findById(id).orElseThrow(() ->
			new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));
	}

	@Override
	public List<OptionValue> findByCategoryOption(CategoryOption categoryOption) {
		return optionValueRepository.findByCategoryOption(categoryOption);
	}
}
