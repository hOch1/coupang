package ecommerce.coupang.service.product.option;

import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.category.CategoryOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryOptionService {

    private final CategoryOptionValueRepository categoryOptionValueRepository;

    /* CategoryOptionValueId로 ProductCategoryOption 생성 */
    public ProductCategoryOption createProductCategoryOption(Long optionValueId, Product product) throws CustomException {
        CategoryOptionValue categoryOptionValue = getCategoryOptionValue(optionValueId);

        return ProductCategoryOption.create(product, categoryOptionValue);
    }

    private CategoryOptionValue getCategoryOptionValue(Long optionValueId) throws CustomException {
        return categoryOptionValueRepository.findById(optionValueId)
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    }
}
