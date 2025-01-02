package ecommerce.coupang.repository.store.custom.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.member.QMemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.domain.store.QCoupon;
import ecommerce.coupang.domain.store.QCouponProduct;
import ecommerce.coupang.domain.store.QStore;
import ecommerce.coupang.dto.request.store.coupon.CouponSort;
import ecommerce.coupang.repository.store.custom.CouponCustomRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CouponCustomRepositoryImpl implements CouponCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<MemberCoupon> findMyCoupons(Long memberId, Pageable pageable, CouponSort sort) {
		QCoupon coupon = QCoupon.coupon;
		QMemberCoupon memberCoupon = QMemberCoupon.memberCoupon;

		JPAQuery<MemberCoupon> query = queryFactory.selectFrom(memberCoupon)
			.join(memberCoupon.coupon, coupon).fetchJoin()
			.where(memberCoupon.member.id.eq(memberId));

		couponSort(query, sort, coupon);
		query.offset(pageable.getOffset()).limit(pageable.getPageSize());
		List<MemberCoupon> result = query.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(memberCoupon.count())
			.from(memberCoupon)
			.where(memberCoupon.member.id.eq(memberId));

		return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<CouponProduct> finCouponsByProduct(Long productVariantId, Pageable pageable, CouponSort sort) {
		QCoupon coupon = QCoupon.coupon;
		QCouponProduct couponProduct = QCouponProduct.couponProduct;

		JPAQuery<CouponProduct> query = queryFactory.selectFrom(couponProduct)
			.join(couponProduct.coupon, coupon).fetchJoin()
			.where(couponProduct.product.id.eq(productVariantId));

		couponSort(query, sort, coupon);
		query.offset(pageable.getOffset()).limit(pageable.getPageSize());
		List<CouponProduct> result = query.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(couponProduct.count())
			.from(couponProduct)
			.where(couponProduct.product.id.eq(productVariantId));

		return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<Coupon> findCouponsByStore(Long storeId, Pageable pageable, CouponSort sort) {
		QCoupon coupon = QCoupon.coupon;

		JPAQuery<Coupon> query = queryFactory.selectFrom(coupon)
			.join(coupon.store, QStore.store).fetchJoin()
			.where(coupon.store.id.eq(storeId));

		couponSort(query, sort, coupon);
		query.offset(pageable.getOffset()).limit(pageable.getPageSize());
		List<Coupon> result = query.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(coupon.count())
			.from(coupon)
			.where(coupon.store.id.eq(storeId));

		return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
	}

	private void couponSort(JPAQuery<?> query, CouponSort sort, QCoupon coupon) {
		switch (sort) {
			case LATEST -> query.orderBy(coupon.createdAt.desc());
			case OLDEST -> query.orderBy(coupon.createdAt.asc());
		}
	}
}
