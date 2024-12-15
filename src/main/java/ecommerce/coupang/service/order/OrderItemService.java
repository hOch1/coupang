package ecommerce.coupang.service.order;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.exception.CustomException;

@LogLevel("OrderItemService")
public interface OrderItemService {

	OrderItem save(Order order, CreateOrderRequest request) throws CustomException;
}
