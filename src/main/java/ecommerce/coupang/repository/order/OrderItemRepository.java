package ecommerce.coupang.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.order.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
