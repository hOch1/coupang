package ecommerce.coupang.dto.request.order;

import java.util.List;

import ecommerce.coupang.domain.order.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderByCartRequest {

	private final List<Long> cartItemIds;
	private final Long addressId;
	private final Payment payment;
	private final String orderMessage;
}
