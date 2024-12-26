package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.response.category.ParentCategoryResponse;
import ecommerce.coupang.dto.response.option.OptionResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final ParentCategoryResponse category; // 현재부터 최상위 까지
	private final StoreResponse store;
	private final int variantCount;
	private final List<OptionResponse> categoryOptions;
	private final List<VariantResponse> variants;

	public static ProductDetailResponse from(Product product) {
		return new ProductDetailResponse(
			product.getId(),
			product.getName(),
			product.getDescription(),
			ParentCategoryResponse.from(product.getCategory()),
			StoreResponse.from(product.getStore()),
			product.getProductVariants().size(),
			product.getProductOptions().stream()
				.map(OptionResponse::productCategoryFrom)
				.toList(),
			product.getProductVariants().stream()
				.map(VariantResponse::from)
				.toList()
		);
	}

	@Getter
	@AllArgsConstructor
	private static class VariantResponse {
		private final Long variantId;
		private final int price;
		private final int stockQuantity;
		private final ProductStatus status;
		private final List<OptionResponse> variantOptions;

		public static VariantResponse from(ProductVariant productVariant) {
			return new VariantResponse(
				productVariant.getId(),
				productVariant.getPrice(),
				productVariant.getStockQuantity(),
				productVariant.getStatus(),
				productVariant.getProductVariantOption().stream()
					.map(OptionResponse::productVariantFrom)
					.toList()
			);
		}
	}
}
