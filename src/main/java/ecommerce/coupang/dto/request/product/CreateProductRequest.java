package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProductRequest {

	private final String name;
	private final String description;
	private final Long categoryId;
	private final Long storeId;
	private final List<CategoryOptionsRequest> categoryOptions;
	private final List<VariantRequest> variants;

	@Getter
	@AllArgsConstructor
	public static class CategoryOptionsRequest {
		private final Long optionId;
		private final Long optionValueId;
	}

	@Getter
	@AllArgsConstructor
	public static class VariantRequest {
		private final int price;
		private final int stock;
		private final ProductStatus status;
		private final boolean isDefault;
		private final List<VariantOption> variantOptions;

		@Getter
		@AllArgsConstructor
		public static class VariantOption {
			private Long optionId;
			private Long optionValueId;
		}
	}
}
