package ecommerce.coupang.service.product.option;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.repository.product.ProductCategoryOptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCategoryOptionService {

	private final CategoryOptionService categoryOptionService;
	private final ProductCategoryOptionRepository productCategoryOptionRepository;

	/* CategoryOptionValueId로 ProductCategoryOption 생성 */
	public ProductCategoryOption createProductCategoryOption(Long optionValueId, Product product) throws CustomException {
		CategoryOptionValue categoryOptionValue = categoryOptionService.getCategoryOptionValue(optionValueId);

		return ProductCategoryOption.create(product, categoryOptionValue);
	}
}
