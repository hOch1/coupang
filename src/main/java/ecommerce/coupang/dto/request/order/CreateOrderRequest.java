package ecommerce.coupang.dto.request.order;

import ecommerce.coupang.domain.order.Payment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class CreateOrderRequest {

	@NotNull(message = "주소를 선택해주세요.")
	private final Long addressId;

	@NotNull(message = "결제방법을 선택해 주세요.")
	private final Payment payment;

	private final String orderMessage;
}
