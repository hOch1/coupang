package ecommerce.coupang.dto.response.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ecommerce.coupang.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParentCategoryResponse {

	private final Long id;
	private final String name;
	private ParentCategoryResponse next;

	public static ParentCategoryResponse from(Category category) {
		List<Category> categories = new ArrayList<>();
		categories.add(category);

		while (category.getParent() != null) {
			categories.add(category.getParent());
			category = category.getParent();
		}

		Collections.reverse(categories);

		ParentCategoryResponse top = new ParentCategoryResponse(categories.get(0).getId(), categories.get(0).getName(), null);
		ParentCategoryResponse current = top;

		for (int i = 1; i < categories.size(); i++) {
			ParentCategoryResponse next = new ParentCategoryResponse(categories.get(i).getId(), categories.get(i).getName(), null);
			current.setNext(next);
			current = next;
		}

		return top;
	}
}
