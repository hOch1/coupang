package ecommerce.coupang.repository.order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.order.Delivery;

@LogLevel("DeliveryRepository")
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	Optional<Delivery> findByCode(String code);
}
