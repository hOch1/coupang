package ecommerce.coupang.dto.response.category;

import ecommerce.coupang.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

	private final Long id;
	private final String name;

	public static CategoryResponse from(Category category) {
		return new CategoryResponse(
			category.getId(),
			category.getName()
		);
	}
}
