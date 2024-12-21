package ecommerce.coupang.dto.response.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final List<OptionValueResponse> values;

	@Getter
	@AllArgsConstructor
	public static class OptionValueResponse {

		private final Long id;
		private final String value;
		private final String description;

	}
}
