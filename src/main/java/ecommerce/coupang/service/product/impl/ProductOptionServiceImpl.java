package ecommerce.coupang.service.product.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.domain.product.ProductOptionValue;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.product.ProductOptionValueRepository;
import ecommerce.coupang.service.product.ProductOptionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductOptionServiceImpl implements ProductOptionService {

	private final ProductOptionValueRepository productOptionValueRepository;

	@Override
	@Transactional
	public ProductOptionValue save(OptionValue optionValue, ProductDetail productDetail) throws CustomException {
		ProductOptionValue productOptionValue = ProductOptionValue.create(optionValue, productDetail);

		return productOptionValueRepository.save(productOptionValue);
	}
}
