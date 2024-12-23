package ecommerce.coupang.service.order;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.exception.CustomException;

@LogLevel("OrderItemService")
public interface OrderItemService {

	OrderItem save(Order order, CreateOrderByProductRequest request) throws CustomException;
}
