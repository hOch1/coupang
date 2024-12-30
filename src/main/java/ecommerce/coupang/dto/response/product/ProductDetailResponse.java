package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.response.category.ParentCategoryResponse;
import ecommerce.coupang.dto.response.option.OptionResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailResponse {

	private final Long productId;
	private final Long productVariantId;
	private final String name;
	private final String description;
	private final int price;
	private final int stockQuantity;
	private final ProductStatus status;
	private final ParentCategoryResponse category;
	private final StoreResponse store;
	private final List<OptionResponse> categoryOptions;
	private final List<OptionResponse> variantOptions;

	public static ProductDetailResponse from(
		ProductVariant productVariant,
		List<ProductCategoryOption> productCategoryOptions,
		List<ProductVariantOption> productVariantOptions) {

		Product product = productVariant.getProduct();
		return new ProductDetailResponse(
			product.getId(),
			productVariant.getId(),
			product.getName(),
			product.getDescription(),
			productVariant.getPrice(),
			productVariant.getStockQuantity(),
			productVariant.getStatus(),
			ParentCategoryResponse.from(product.getCategory()),
			StoreResponse.from(product.getStore()),
			productCategoryOptions.stream()
				.map(OptionResponse::productCategoryFrom)
				.toList(),
			productVariantOptions.stream()
				.map(OptionResponse::productVariantFrom)
				.toList()
		);
	}
}
