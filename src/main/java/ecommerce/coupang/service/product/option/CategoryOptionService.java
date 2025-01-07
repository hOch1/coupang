package ecommerce.coupang.service.product.option;

import ecommerce.coupang.domain.category.CategoryOptionValue;
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

    public CategoryOptionValue getCategoryOptionValue(Long optionValueId) throws CustomException {
        return categoryOptionValueRepository.findById(optionValueId)
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    }
}
