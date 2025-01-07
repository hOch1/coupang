package ecommerce.coupang.service.product.option;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
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

		return ProductVariantOption.create(productVariant, variantOptionValue);
	}

	/* ProductVariantOption 조회 */
	public List<ProductVariantOption> getProductVariantOptionByProductVariantId(Long productVariantId) {
		return productVariantOptionRepository.findByProductVariantId(productVariantId);
	}
}
