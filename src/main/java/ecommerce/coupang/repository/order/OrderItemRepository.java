package ecommerce.coupang.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.order.OrderItem;

@LogLevel("OrderItemRepository")
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("select oi from OrderItem oi "
		+ "join fetch oi.productVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch oi.delivery d "
		+ "where oi.order.id = :orderId")
	List<OrderItem> findByOrderId(Long orderId);
}
