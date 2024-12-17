package ecommerce.coupang.dto.response.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.product.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

	private final Long id;
	private final String type;
	private final String name;
	private final List<CategoryResponse> children;

	public static CategoryResponse from(Category category, boolean includeChildren) {
		return new CategoryResponse(
			category.getId(),
			category.getType(),
			category.getName(),
			includeChildren ?
				category.getChildren().stream()
				.map(c -> CategoryResponse.from(c, true))
				.toList() : null
		);
	}
}
