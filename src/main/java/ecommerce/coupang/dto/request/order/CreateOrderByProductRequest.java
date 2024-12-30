package ecommerce.coupang.dto.request.order;

import ecommerce.coupang.domain.order.Payment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderByProductRequest {

	@NotNull(message = "상품을 선택해주세요.")
	private final Long productVariantId;

	@NotNull(message = "주소를 선택해주세요.")
	private final Long addressId;

	@Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
	private final int quantity;

	@NotNull(message = "결제방법을 선택해 주세요.")
	private final Payment payment;

	private final String orderMessage;
}
