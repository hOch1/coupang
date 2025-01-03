package ecommerce.coupang.repository.product.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.QCategory;
import ecommerce.coupang.domain.member.QMember;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.QProduct;
import ecommerce.coupang.domain.product.QProductCategoryOption;
import ecommerce.coupang.domain.product.variant.QProductVariant;
import ecommerce.coupang.domain.product.variant.QProductVariantOption;
import ecommerce.coupang.domain.store.QCouponProduct;
import ecommerce.coupang.domain.store.QStore;
import ecommerce.coupang.dto.request.product.ProductSort;
import ecommerce.coupang.dto.response.category.CategoryResponse;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import ecommerce.coupang.repository.product.custom.ProductCustomRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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
	public Page<ProductResponse> searchProducts(List<Category> categories, Long storeId, List<Long> categoryOptions, List<Long> variantOptions, ProductSort sort, Pageable pageable) {
		QProduct product = QProduct.product;
		QProductVariant productVariant = QProductVariant.productVariant;
		QProductCategoryOption productCategoryOption = QProductCategoryOption.productCategoryOption;
		QProductVariantOption productVariantOption = QProductVariantOption.productVariantOption;
		QCategory category = QCategory.category;
		QStore store = QStore.store;
		QCouponProduct couponProduct = QCouponProduct.couponProduct;

		BooleanBuilder builder = searchFilter(productVariant, categories, storeId, categoryOptions, variantOptions, product, productCategoryOption, productVariantOption);

		JPAQuery<ProductResponse> query = queryFactory
			.select(Projections.constructor(
				ProductResponse.class,
				product.id,
				productVariant.id,
				product.name,
				productVariant.price,
				productVariant.status,
				Projections.constructor(
					StoreResponse.class,
					store.id,
					store.name
				),
				Projections.constructor(
					CategoryResponse.class,
					category.id,
					category.name
				),
				JPAExpressions.selectOne()
					.from(couponProduct)
					.where(couponProduct.product.id.eq(product.id))
					.exists()
			))
			.from(productVariant)
			.join(productVariant.product, product).fetchJoin()
			.join(productVariant.product.store, store).fetchJoin()
			.join(productVariant.product.category, category).fetchJoin()
			.where(builder);

		searchSort(query, product, productVariant, sort);

		query.offset(pageable.getOffset()).limit(pageable.getPageSize());

		List<ProductResponse> result = query.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(productVariant.count())
			.from(productVariant)
			.where(builder);

		return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
	}

	private void searchSort(JPQLQuery<ProductResponse> query, QProduct product, QProductVariant productVariant, ProductSort sort) {
		switch (sort) {
			case LATEST -> query.orderBy(product.createdAt.desc());
			case OLDEST -> query.orderBy(product.createdAt.asc());
			case REVIEW -> query.orderBy(product.reviewCount.desc());
			case RATING -> query.orderBy(product.starAvg.desc());
			case PRICE_HIGH -> query.orderBy(productVariant.price.desc());
			case PRICE_LOW -> query.orderBy(productVariant.price.asc());
			case SALES -> query.orderBy(productVariant.salesCount.desc());
		}
	}

	private BooleanBuilder searchFilter(QProductVariant productVariant, List<Category> categories, Long storeId, List<Long> categoryOptions, List<Long> variantOptions, QProduct product, QProductCategoryOption productCategoryOption, QProductVariantOption productVariantOption) {

		BooleanBuilder builder = new BooleanBuilder(productVariant.isDefault.isTrue());

		if (!categories.isEmpty())
			builder.and(product.category.in(categories));
		if (storeId != null)
			builder.and(product.store.id.eq(storeId));
		if (categoryOptions != null && !categoryOptions.isEmpty())
			builder.and(productCategoryOption.categoryOptionValue.id.in(categoryOptions));
		if (variantOptions != null && !variantOptions.isEmpty())
			builder.and(productVariantOption.variantOptionValue.id.in(variantOptions));

		return builder;
	}
}
