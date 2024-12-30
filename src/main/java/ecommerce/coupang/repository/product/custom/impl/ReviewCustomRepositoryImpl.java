package ecommerce.coupang.repository.product.custom.impl;

import java.util.List;

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
	public List<ProductReview> findByProductId(Long productId, Integer star, ReviewSort sort) {
		QProductReview productReview = QProductReview.productReview;
		QMember member = QMember.member;
		QProduct product = QProduct.product;

		BooleanBuilder builder = new BooleanBuilder();

		if (star != null)
			builder.and(productReview.star.eq(star));

		JPAQuery<ProductReview> query = queryFactory.selectDistinct(productReview)
			.from(productReview)
			.join(productReview.member, member).fetchJoin()
			.join(productReview.product, product).fetchJoin()
			.where(product.id.eq(productId)
				.and(builder));

		sortReview(query, sort, productReview);

		return query.fetch();
	}

	private void sortReview(JPAQuery<ProductReview> query, ReviewSort sort, QProductReview productReview) {
		switch (sort) {
			case LATEST -> query.orderBy(productReview.createdAt.desc());
			case OLDEST -> query.orderBy(productReview.createdAt.asc());
			case LIKES -> query.orderBy(productReview.likeCount.desc());
		}
	}
}
