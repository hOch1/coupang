package ecommerce.coupang.repository.order.custom.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ecommerce.coupang.domain.member.QAddress;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderStatus;
import ecommerce.coupang.domain.order.QDelivery;
import ecommerce.coupang.domain.order.QOrder;
import ecommerce.coupang.dto.request.order.OrderSearchStatus;
import ecommerce.coupang.dto.request.order.OrderSort;
import ecommerce.coupang.repository.order.custom.OrderCustomRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Order> findOrders(Long memberId, OrderStatus status, OrderSort sort) {
		QOrder order = QOrder.order;
		QAddress address = QAddress.address1;

		BooleanBuilder builder = new BooleanBuilder();

		if (status != null)
			builder.and(order.status.eq(status));

		JPAQuery<Order> query = queryFactory.selectDistinct(order)
			.from(order)
			.join(order.address, address).fetchJoin()
			.where(order.member.id.eq(memberId)
				.and(builder));

		return query.fetch();
	}

	private void orderSort(JPAQuery<Order> query, OrderSort sort, QOrder order) {
		switch (sort) {
			case LATEST -> query.orderBy(order.createdAt.desc());
			case OLDEST -> query.orderBy(order.createdAt.asc());
			case PRICE_LOW -> query.orderBy(order.totalPrice.asc());
			case PRICE_HIGH -> query.orderBy(order.totalPrice.desc());
		}
	}
}
