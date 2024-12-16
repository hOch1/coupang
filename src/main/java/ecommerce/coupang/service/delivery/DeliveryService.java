package ecommerce.coupang.service.delivery;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.order.Delivery;

@LogLevel("DeliveryService")
public interface DeliveryService {

	Delivery createDelivery();
}
