package ecommerce.coupang.service.product.option;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.category.VariantOptionValue;
import ecommerce.coupang.dto.request.product.option.OptionRequest;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import ecommerce.coupang.service.category.VariantOptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductVariantOptionService {

	private final VariantOptionService variantOptionService;
	private final ProductVariantOptionRepository productVariantOptionRepository;

	/* VariantOptionId로 ProductVariantOption 생성 */
	public ProductVariantOption createProductVariantOption(Long variantOptionValueId, ProductVariant productVariant) throws CustomException {
		VariantOptionValue variantOptionValue = variantOptionService.getVariantOptionValue(variantOptionValueId);

		return ProductVariantOption.of(productVariant, variantOptionValue);
	}

	/* ProductVariantOption 조회 */
	public List<ProductVariantOption> getProductVariantOptionByProductVariantId(Long productVariantId) {
		return productVariantOptionRepository.findByProductVariantId(productVariantId);
	}

	/* 상품 변형 옵션 변경 */
	public void update(Long optionId, Long optionValueId, ProductVariant productVariant) throws CustomException {
		ProductVariantOption productVariantOption = getPvoByProductVariantIdAndCategoryOptionId(
			productVariant.getId(), optionId);

		VariantOptionValue variantOptionValue = variantOptionService.getVariantOptionValue(optionValueId);

		productVariantOption.update(variantOptionValue);
	}

	private ProductVariantOption getPvoByProductVariantIdAndCategoryOptionId(Long productVariantId, Long variantOptionId) {
		return productVariantOptionRepository.findByProductIdAndCategoryOptionId(productVariantId, variantOptionId);
	}
}
