package ecommerce.coupang.service.product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.product.variant.VariantOption;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.option.CategoryOptionsRequest;
import ecommerce.coupang.dto.request.product.option.VariantOptionRequest;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.service.category.CategoryOptionService;
import ecommerce.coupang.service.category.VariantOptionService;
import ecommerce.coupang.service.product.option.ProductCategoryOptionService;
import ecommerce.coupang.service.product.option.ProductVariantOptionService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCreateManagement {

    private final ProductVariantOptionService productVariantOptionService;
    private final ProductCategoryOptionService productCategoryOptionService;
    private final CategoryOptionService categoryOptionService;
    private final VariantOptionService variantOptionService;

    /* 상품 생성, 상품 변형 추가, 옵션 추가 */
    public Product createProductAndVariantAndOptions(CreateProductRequest request, Store store, Category category) throws CustomException {
        validateProductRequestContainsNeedOptions(request);
        Product product = Product.create(request, store, category);

        for (CategoryOptionsRequest categoryOption : request.getCategoryOptions())
            addCategoryOptionsToProduct(categoryOption.getOptionValueId(), product);

        for (CreateProductVariantRequest variantRequest : request.getVariants())
            createVariantAndOptions(variantRequest, product);

        return product;
    }

    /* 상품 변형 생성, 옵션 추가 */
    public ProductVariant createVariantAndOptions(CreateProductVariantRequest request, Product product) throws CustomException {
        validateVariantRequestContainsNeedOptions(request, product.getCategory().getId());
        ProductVariant productVariant = ProductVariant.create(request, product);

        for (VariantOptionRequest variantOption : request.getVariantOptions()) {
            addVariantOptionToProductVariant(variantOption.getOptionValueId(), productVariant);

            product.addProductVariants(productVariant);
        }

        return productVariant;
    }

    /* ProductVariantOption -> ProductVariant 추가 */
    public void addVariantOptionToProductVariant(Long optionValueId, ProductVariant productVariant) throws CustomException {
        ProductVariantOption productVariantOption = productVariantOptionService.createProductVariantOption(optionValueId, productVariant);

        productVariant.addProductVariantOptions(productVariantOption);
    }

    /* ProductOption -> Product 추가 */
    public void addCategoryOptionsToProduct(Long optionValueId, Product product) throws CustomException {
        ProductCategoryOption productCategoryOption = productCategoryOptionService.createProductCategoryOption(optionValueId, product);

        product.addProductOptions(productCategoryOption);
    }

    /* request 에 categoryOption 이 누락되었는지 검증 */
    private void validateProductRequestContainsNeedOptions(CreateProductRequest request) throws CustomException {
        List<CategoryOption> categoryOptions = categoryOptionService.getCategoryOption(request.getCategoryId());

        Set<Long> needOptionIdSet = categoryOptions.stream()
            .map(CategoryOption::getId)
            .collect(Collectors.toSet());

        request.getCategoryOptions()
            .forEach(co -> needOptionIdSet.remove(co.getOptionId()));

        if (!needOptionIdSet.isEmpty())
            throw new CustomException(ErrorCode.OPTION_NOT_CONTAINS); // TODO 예외 구체화
    }

    /* request 에 variantOption 이 누락되었는지 검증 */
    private void validateVariantRequestContainsNeedOptions(CreateProductVariantRequest request, Long categoryId) throws CustomException {
        List<VariantOption> variantOptions = variantOptionService.getVariantOption(categoryId);

        Set<Long> needOptionIdSet = variantOptions.stream()
            .map(VariantOption::getId)
            .collect(Collectors.toSet());

        request.getVariantOptions()
            .forEach(vo -> needOptionIdSet.remove(vo.getOptionId()));

        if (!needOptionIdSet.isEmpty())
            throw new CustomException(ErrorCode.OPTION_NOT_CONTAINS); // TODO 예외 구체화
    }
}
