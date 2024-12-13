package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.CategoryOption;

public interface CategoryOptionService {

	List<CategoryOption> findByCategory(Category category);
}
