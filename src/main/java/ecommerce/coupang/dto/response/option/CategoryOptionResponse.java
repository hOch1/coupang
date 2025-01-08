package ecommerce.coupang.dto.response.option;

import java.util.List;

import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.domain.category.CategoryOptionValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryOptionResponse {
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
