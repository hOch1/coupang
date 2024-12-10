package ecommerce.coupang.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.order.Delivery;

@LogLevel("DeliveryRepository")
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
