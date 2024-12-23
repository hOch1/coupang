package ecommerce.coupang.service.product.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.dto.response.option.OptionResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.service.product.OptionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

	@Override
	public List<OptionResponse> findCategoryOption(Long categoryId) throws CustomException {
		return List.of();
	}
}
