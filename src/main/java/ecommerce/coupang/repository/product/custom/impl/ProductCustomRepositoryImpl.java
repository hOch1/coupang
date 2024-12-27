package ecommerce.coupang.repository.product.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.QCategory;
import ecommerce.coupang.domain.product.QProduct;
import ecommerce.coupang.domain.product.QProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.QProductVariant;
import ecommerce.coupang.domain.product.variant.QProductVariantOption;
import ecommerce.coupang.domain.store.QStore;
import ecommerce.coupang.repository.product.custom.ProductCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ProductVariant> searchProducts(List<Category> categories, Long storeId, List<Long> categoryOptions, List<Long> variantOptions) {
		QProduct product = QProduct.product;
		QProductVariant productVariant = QProductVariant.productVariant;
		QProductCategoryOption productCategoryOption = QProductCategoryOption.productCategoryOption;
		QProductVariantOption productVariantOption = QProductVariantOption.productVariantOption;
		QCategory category = QCategory.category;
		QStore store = QStore.store;

		BooleanBuilder builder = new BooleanBuilder();

		builder.and(productVariant.isDefault.isTrue());

		if (!categories.isEmpty())
			builder.and(product.category.in(categories));
		if (storeId != null)
			builder.and(product.store.id.eq(storeId));
		if (categoryOptions != null)
			builder.and(productCategoryOption.categoryOptionValue.id.in(categoryOptions));
		if (variantOptions != null)
			builder.and(productVariantOption.variantOptionValue.id.in(variantOptions));

		return queryFactory
			.selectDistinct(productVariant)
			.from(productVariant)
			.join(productVariant.product, product).fetchJoin()
			.join(productVariant.product.store, store).fetchJoin()
			.join(productVariant.product.category, category).fetchJoin()
			.where(builder)
			.fetch();
	}
}
