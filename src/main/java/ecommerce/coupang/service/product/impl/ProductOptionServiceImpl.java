package ecommerce.coupang.service.product.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.domain.product.ProductOption;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.product.ProductOptionRepository;
import ecommerce.coupang.service.product.ProductOptionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductOptionServiceImpl implements ProductOptionService {

	private final ProductOptionRepository productOptionRepository;

	@Override
	@Transactional
	public ProductOption save(OptionValue optionValue, ProductDetail productDetail) throws CustomException {
		ProductOption productOption = ProductOption.create(optionValue, productDetail);

		return productOptionRepository.save(productOption);
	}
}
