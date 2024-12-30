package ecommerce.coupang.dto.request.order;

import java.util.List;

import ecommerce.coupang.domain.order.Payment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderByCartRequest {

	@NotEmpty(message = "상품을 하나이상 선택해주세요.")
	private final List<Long> cartItemIds;

	@NotNull(message = "주소를 선택해주세요.")
	private final Long addressId;

	@NotNull(message = "결제방법을 선택해 주세요.")
	private final Payment payment;

	private final String orderMessage;
}
