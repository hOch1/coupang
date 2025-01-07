package ecommerce.coupang.dto.response.option;

import java.util.List;

import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.product.variant.VariantOption;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllOptionResponse {

	private List<CategoryOptionResponse> categoryOptions;
	private List<VariantOptionResponse> variantOptions;

	public void setOptions(List<CategoryOptionResponse> categoryOptions, List<VariantOptionResponse> variantOptions) {
		this.categoryOptions = categoryOptions;
		this.variantOptions = variantOptions;
	}

	// TODO 분리
	@Data
	@AllArgsConstructor
	public static class CategoryOptionResponse {

		private Long optionId;
		private String optionName;
		private List<CategoryOptionValueResponse> values;

		public static CategoryOptionResponse from(CategoryOption categoryOption) {
			return new CategoryOptionResponse(
				categoryOption.getId(),
				categoryOption.getDescription(),
				categoryOption.getCategoryOptionValues().stream()
					.map(CategoryOptionValueResponse::from)
					.toList()
				);
		}

		@Data
		@AllArgsConstructor
		public static class CategoryOptionValueResponse {

			private Long optionValueId;
			private String value;

			public static CategoryOptionValueResponse from(CategoryOptionValue categoryOptionValue) {
				return new CategoryOptionValueResponse(
					categoryOptionValue.getId(),
					categoryOptionValue.getDescription()
				);
			}

		}
	}

	@Data
	@AllArgsConstructor
	public static class VariantOptionResponse {
		private Long optionId;
		private String optionName;
		private List<VariantOptionValueResponse> values;

		public static VariantOptionResponse from(VariantOption variantOption) {
			return new VariantOptionResponse(
				variantOption.getId(),
				variantOption.getDescription(),
				variantOption.getVariantOptionValues().stream()
					.map(VariantOptionResponse.VariantOptionValueResponse::from)
					.toList()
			);
		}

		@Data
		@AllArgsConstructor
		public static class VariantOptionValueResponse {

			private Long optionValueId;
			private String value;

			public static VariantOptionValueResponse from(VariantOptionValue variantOptionValue) {
				return new VariantOptionValueResponse(
					variantOptionValue.getId(),
					variantOptionValue.getDescription()
				);
			}
		}
	}
}
