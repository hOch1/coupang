package ecommerce.coupang.service.product.option;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import ecommerce.coupang.repository.product.VariantOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VariantOptionService {

    private final ProductVariantOptionRepository productVariantOptionRepository;
    private final VariantOptionValueRepository variantOptionValueRepository;

    /* VariantOptionId로 ProductVariantOption 생성 */
    public ProductVariantOption createProductVariantOption(Long variantOptionValueId, ProductVariant productVariant) throws CustomException {
        VariantOptionValue variantOptionValue = getVariantOptionValue(variantOptionValueId);

        return ProductVariantOption.create(productVariant, variantOptionValue);
    }

    /* ProductVariantOption 조회 */
    public List<ProductVariantOption> getProductVariantOptionByProductVariantId(Long productVariantId) {
        return productVariantOptionRepository.findByProductVariantId(productVariantId);
    }

    private VariantOptionValue getVariantOptionValue(Long optionValueId) throws CustomException {
        return variantOptionValueRepository.findById(optionValueId)
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    }

}
