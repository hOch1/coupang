package ecommerce.coupang.service.category;


import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.variant.VariantOption;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.VariantOptionValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VariantOptionService {

    private final VariantOptionValueRepository variantOptionValueRepository;
    private final CategoryService categoryService;

    public VariantOptionValue getVariantOptionValue(Long optionValueId) throws CustomException {
        return variantOptionValueRepository.findById(optionValueId)
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    }

    public List<VariantOption> getVariantOption(Long categoryId) throws CustomException {
        Category category = categoryService.findCategoryWithRoot(categoryId);
        List<VariantOption> variantOptions = new ArrayList<>();

        while (category != null) {
            variantOptions.addAll(category.getVariantOptions());
            category = category.getParent();
        }

        return variantOptions;
    }
}
