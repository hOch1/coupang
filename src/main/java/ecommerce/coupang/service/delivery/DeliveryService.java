package ecommerce.coupang.service.delivery;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.domain.order.OrderItem;

@LogLevel("DeliveryService")
public interface DeliveryService {

	Delivery createDelivery(OrderItem orderItem, Store store);
}
