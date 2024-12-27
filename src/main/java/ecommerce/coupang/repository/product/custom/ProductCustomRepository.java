package ecommerce.coupang.repository.product.custom;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.variant.ProductVariant;

import java.util.List;

public interface ProductCustomRepository {

	List<ProductVariant> searchProducts(List<Category> categories, Long storeId, List<Long> categoryOptions, List<Long> variantOptions);
}
