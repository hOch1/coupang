package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final CategoryResponse category; // 현재부터 최상위까지만
	private final StoreResponse store; // 상점 ID, 이름
	private final List<CategoryOptionsResponse> categoryOptions;
	private final List<VariantResponse> variants;

	public static ProductDetailResponse from(Product product) {
		return new ProductDetailResponse(
			product.getId(),
			product.getName(),
			product.getDescription(),
			CategoryResponse.from(product.getCategory(), false),
			StoreResponse.from(product.getStore(), false),
			product.getProductOptions().stream()
				.map(CategoryOptionsResponse::from)
				.toList(),
			product.getProductVariants().stream()
				.map(VariantResponse::from)
				.toList()
		);
	}

	@Getter
	@AllArgsConstructor
	private static class CategoryOptionsResponse {
		private final Long optionId;
		private final String optionName;
		private final Long optionValueId;
		private final String optionValue;

		public static CategoryOptionsResponse from(ProductCategoryOption productCategoryOption) {
			return new CategoryOptionsResponse(
				productCategoryOption.getCategoryOptionValue().getCategoryOption().getId(),
				productCategoryOption.getCategoryOptionValue().getCategoryOption().getOptionName(),
				productCategoryOption.getCategoryOptionValue().getId(),
				productCategoryOption.getCategoryOptionValue().getValue()
			);
		}
	}

	@Getter
	@AllArgsConstructor
	private static class VariantResponse {
		private final Long variantId;
		private final int price;
		private final int stockQuantity;
		private final ProductStatus status;
		private final List<VariantOptionResponse> variantOptions;

		public static VariantResponse from(ProductVariant productVariant) {
			return new VariantResponse(
				productVariant.getId(),
				productVariant.getPrice(),
				productVariant.getStockQuantity(),
				productVariant.getStatus(),
				productVariant.getProductVariantOption().stream()
					.map(VariantOptionResponse::from)
					.toList()
			);
		}

		@Getter
		@AllArgsConstructor
		public static class VariantOptionResponse {
			private final Long optionId;
			private final String optionName;
			private final Long optionValueId;
			private final String optionValue;

			public static VariantOptionResponse from(ProductVariantOption productVariantOption) {
				return new VariantOptionResponse(
					productVariantOption.getVariantOptionValue().getVariantOption().getId(),
					productVariantOption.getVariantOptionValue().getVariantOption().getOptionName(),
					productVariantOption.getVariantOptionValue().getId(),
					productVariantOption.getVariantOptionValue().getValue()
				);
			}
		}
	}
}
