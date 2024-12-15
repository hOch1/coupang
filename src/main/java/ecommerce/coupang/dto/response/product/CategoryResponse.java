package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.product.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

	private final Long id;
	private final String type;
	private final String name;
	private final List<CategoryResponse> children;

	public static CategoryResponse from(Category category) {
		List<CategoryResponse> list = category.getChildren().stream()
			.map(CategoryResponse::from)
			.toList();

		return new CategoryResponse(
			category.getId(),
			category.getType(),
			category.getName(),
			list
		);
	}
}
