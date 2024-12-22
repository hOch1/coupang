package ecommerce.coupang.dto.response.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

	private final Long id;
	private final String name;
	private final List<CategoryResponse> children;
	private final CategoryResponse parent;

	public static CategoryResponse includeChildrenFrom(Category category) {
		return new CategoryResponse(
			category.getId(),
			category.getType(),
			category.getChildren().stream()
				.map(CategoryResponse::includeChildrenFrom)
				.toList(),
			null
		);
	}

	public static CategoryResponse includeParentFrom(Category category) {
		// TODO 루트 카테고리 -> 현재 카테고리 순서로 변경해야함
		return new CategoryResponse(
			category.getId(),
			category.getName(),
			null,
			category.getParent() != null ?
				CategoryResponse.includeParentFrom(category.getParent()) : null
		);
	}
}
