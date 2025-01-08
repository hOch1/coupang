package ecommerce.coupang.dto.response.option;

import java.util.List;

import ecommerce.coupang.domain.category.VariantOption;
import ecommerce.coupang.domain.category.VariantOptionValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VariantOptionResponse {

	private Long optionId;
	private String optionName;
	private List<VariantOptionValueResponse> values;

	public static VariantOptionResponse from(VariantOption variantOption) {
		return new VariantOptionResponse(
			variantOption.getId(),
			variantOption.getDescription(),
			variantOption.getVariantOptionValues().stream()
				.map(VariantOptionValueResponse::from)
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
