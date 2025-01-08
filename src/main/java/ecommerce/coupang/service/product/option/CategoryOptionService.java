package ecommerce.coupang.service.product.option;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.category.CategoryOptionValueRepository;
import ecommerce.coupang.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryOptionService {

    private final CategoryOptionValueRepository categoryOptionValueRepository;
    private final CategoryService categoryService;

    public CategoryOptionValue getCategoryOptionValue(Long optionValueId) throws CustomException {
        return categoryOptionValueRepository.findById(optionValueId)
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));
    }

    public List<CategoryOption> getCategoryOption(Long categoryId) throws CustomException {
        Category category = categoryService.findCategoryWithRoot(categoryId);
        List<CategoryOption> categoryOptions = new ArrayList<>();

        while (category != null) {
            categoryOptions.addAll(category.getCategoryOptions());
            category = category.getParent();
        }

        return categoryOptions;
    }
}
