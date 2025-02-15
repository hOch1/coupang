package ecommerce.coupang.service.product.option;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.dto.request.product.option.OptionRequest;
import ecommerce.coupang.repository.product.ProductCategoryOptionRepository;
import ecommerce.coupang.service.category.CategoryOptionService;
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

		return ProductCategoryOption.of(product, categoryOptionValue);
	}

	/* ProductCategoryOption 조회 */
	public ProductCategoryOption getPcoByProductIdAndCategoryOptionId(Long productId, Long categoryOptionId) {
		return productCategoryOptionRepository.findByProductIdAndCategoryOptionId(productId, categoryOptionId);
	}

	/* 상품 옵션 변경 */
	public void update(Long optionId, Long optionValueId, Product product) throws CustomException {
		ProductCategoryOption productCategoryOption = getPcoByProductIdAndCategoryOptionId(product.getId(), optionId);

		CategoryOptionValue categoryOptionValue = categoryOptionService.getCategoryOptionValue(optionValueId);

		productCategoryOption.update(categoryOptionValue);
	}
}
