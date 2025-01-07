package ecommerce.coupang.service.product;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.request.product.option.VariantOptionRequest;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.product.option.ProductVariantOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantFactory {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantOptionService productVariantOptionService;

    /* 상품 변경 생성, 옵션 추가 */
    public ProductVariant createVariantAndOptions(CreateProductVariantRequest request, Product product) throws CustomException {
        ProductVariant productVariant = ProductVariant.create(request, product);

        addVariantOptionToProductVariant(request.getVariantOptions(), productVariant);

        product.addProductVariants(productVariant);

        productVariantRepository.save(productVariant);
        return productVariant;
    }

    /* ProductVariantOption -> ProductVariant 추가 */
    public void addVariantOptionToProductVariant(List<VariantOptionRequest> requests, ProductVariant productVariant) throws CustomException {
        for (VariantOptionRequest request : requests) {
            ProductVariantOption productVariantOption = productVariantOptionService.createProductVariantOption(request.getOptionValueId(), productVariant);

            productVariant.addProductVariantOptions(productVariantOption);
        }
    }
}
