package ecommerce.coupang.service.product;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.option.CategoryOptionsRequest;
import ecommerce.coupang.dto.request.product.option.VariantOptionRequest;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.service.product.option.ProductCategoryOptionService;
import ecommerce.coupang.service.product.option.ProductVariantOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCreateManagement {

    private final ProductVariantOptionService productVariantOptionService;
    private final ProductCategoryOptionService productCategoryOptionService;

    /* 상품 생성, 상품 변형 추가, 옵션 추가 */
    public Product createProductAndVariantAndOptions(CreateProductRequest request, Store store, Category category) throws CustomException {
        Product product = Product.create(request, store, category);

        for (CategoryOptionsRequest categoryOption : request.getCategoryOptions())
            addCategoryOptionsToProduct(categoryOption.getOptionValueId(), product);

        for (CreateProductVariantRequest variantRequest : request.getVariants())
            createVariantAndOptions(variantRequest, product);

        return product;
    }

    /* 상품 변형 생성, 옵션 추가 */
    public ProductVariant createVariantAndOptions(CreateProductVariantRequest request, Product product) throws CustomException {
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
}
