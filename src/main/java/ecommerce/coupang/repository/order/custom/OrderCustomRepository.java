package ecommerce.coupang.repository.order.custom;

import java.util.List;

import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderStatus;
import ecommerce.coupang.dto.request.order.OrderSort;

public interface OrderCustomRepository {

	List<Order> findOrders(Long memberId, OrderStatus status, OrderSort sort);
}
