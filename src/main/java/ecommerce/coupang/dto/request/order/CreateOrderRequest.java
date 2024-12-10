package ecommerce.coupang.dto.request.order;

import ecommerce.coupang.domain.order.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderRequest {

	private final Payment payment;
	private final String orderMessage;
}
