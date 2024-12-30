package ecommerce.coupang.repository.product.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.QCategory;
import ecommerce.coupang.domain.member.QMember;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.QProduct;
import ecommerce.coupang.domain.product.QProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.QProductVariant;
import ecommerce.coupang.domain.product.variant.QProductVariantOption;
import ecommerce.coupang.domain.store.QStore;
import ecommerce.coupang.repository.product.custom.ProductCustomRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Product> findByIdWithMemberAndCategory(Long productId) {
		QProduct product = QProduct.product;
		QStore store = QStore.store;
		QMember member = QMember.member;
		QCategory category = QCategory.category;

		Product result = queryFactory.selectDistinct(product)
			.from(product)
			.join(product.store, store).fetchJoin()
			.join(store.member, member).fetchJoin()
			.join(product.category, category).fetchJoin()
			.where(product.id.eq(productId)
				.and(product.isActive.isTrue())
				.and(store.isActive.isTrue()))
			.fetchOne();

		return Optional.ofNullable(result);
	}

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
