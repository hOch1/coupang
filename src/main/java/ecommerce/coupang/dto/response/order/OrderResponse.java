package ecommerce.coupang.dto.response.order;

import java.time.LocalDateTime;

import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {

	private final Long id;
	private final String address;
	private final OrderStatus status;
	private final int totalPrice;
	private final LocalDateTime createdAt;

	public static OrderResponse from(Order order) {
		return new OrderResponse(
			order.getId(),
			order.getAddress().getAddress(),
			order.getStatus(),
			order.getTotalPrice(),
			order.getCreatedAt()
		);
	}
}
