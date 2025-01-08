package ecommerce.coupang.repository.category.custom;

import java.util.Optional;

import ecommerce.coupang.domain.category.Category;

public interface CategoryCustomRepository {

	Optional<Category> findCategoryWithRoot(Long categoryId);

}
