package ecommerce.coupang.dto.response.category;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubCategoryResponse {

	private final Long id;
	private final String name;
	private final int childrenCount;
	private final List<SubCategoryResponse> children;

	public static SubCategoryResponse from(Category category) {
		return new SubCategoryResponse(
			category.getId(),
			category.getType(),
			category.getChildren().size(),
			category.getChildren().stream()
				.map(SubCategoryResponse::from)
				.toList()
		);
	}
}
