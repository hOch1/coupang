package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.domain.product.OptionValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final List<OptionValueResponse> values;

	public static OptionResponse from(CategoryOption categoryOption, List<OptionValue> optionValues) {
		List<OptionValueResponse> values = optionValues.stream()
			.map(OptionValueResponse::from)
			.toList();

		return new OptionResponse(
			categoryOption.getId(),
			categoryOption.getOptionName(),
			categoryOption.getDescription(),
			values
		);
	}

	@Getter
	@AllArgsConstructor
	public static class OptionValueResponse {

		private final Long id;
		private final String value;
		private final String description;

		public static OptionValueResponse from(OptionValue optionValue) {
			return new OptionValueResponse(
				optionValue.getId(),
				optionValue.getValue(),
				optionValue.getDescription()
			);
		}
	}
}
