package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.product.sub.CategorySubOption;
import ecommerce.coupang.domain.product.sub.SubOptionValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final List<OptionValueResponse> values;

	public static OptionResponse from(CategorySubOption categorySubOption, List<SubOptionValue> subOptionValues) {
		List<OptionValueResponse> values = subOptionValues.stream()
			.map(OptionValueResponse::from)
			.toList();

		return new OptionResponse(
			categorySubOption.getId(),
			categorySubOption.getOptionName(),
			categorySubOption.getDescription(),
			values
		);
	}

	@Getter
	@AllArgsConstructor
	public static class OptionValueResponse {

		private final Long id;
		private final String value;
		private final String description;

		public static OptionValueResponse from(SubOptionValue subOptionValue) {
			return new OptionValueResponse(
				subOptionValue.getId(),
				subOptionValue.getValue(),
				subOptionValue.getDescription()
			);
		}
	}
}
