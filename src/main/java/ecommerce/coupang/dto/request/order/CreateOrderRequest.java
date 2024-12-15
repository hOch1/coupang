package ecommerce.coupang.dto.request.order;

import ecommerce.coupang.domain.order.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderRequest {

	private final Long productId;
	private final Long addressId;
	private final int quantity;
	private final Payment payment;
	private final String orderMessage;
}
