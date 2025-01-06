package ecommerce.coupang.repository.product.custom.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ecommerce.coupang.domain.member.QMember;
import ecommerce.coupang.domain.product.QProduct;
import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.domain.product.review.QProductReview;
import ecommerce.coupang.dto.request.product.review.ReviewSort;
import ecommerce.coupang.repository.product.custom.ReviewCustomRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<ProductReview> findByProductId(Long productId, Integer star, ReviewSort sort, Pageable pageable) {
		QProductReview productReview = QProductReview.productReview;
		QMember member = QMember.member;
		QProduct product = QProduct.product;

		BooleanBuilder builder = reviewFilter(productId, star, product, productReview);

		JPAQuery<ProductReview> query = queryFactory.selectDistinct(productReview)
			.from(productReview)
			.join(productReview.member, member).fetchJoin()
			.join(productReview.product, product).fetchJoin()
			.join(member.cart).fetchJoin()
			.where(builder);

		sortReview(query, sort, productReview);

		query.offset(pageable.getOffset()).limit(pageable.getPageSize());
		List<ProductReview> result = query.fetch();

		JPAQuery<Long> countQuery = queryFactory.select(productReview.count())
			.from(productReview)
			.where(builder);

		return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
	}

	private static BooleanBuilder reviewFilter(Long productId, Integer star, QProduct product, QProductReview productReview) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(product.id.eq(productId));

		if (star != null)
			builder.and(productReview.star.eq(star));

		return builder;
	}

	private void sortReview(JPAQuery<ProductReview> query, ReviewSort sort, QProductReview productReview) {
		switch (sort) {
			case LATEST -> query.orderBy(productReview.createdAt.desc());
			case OLDEST -> query.orderBy(productReview.createdAt.asc());
			case LIKES -> query.orderBy(productReview.likeCount.desc());
		}
	}
}
