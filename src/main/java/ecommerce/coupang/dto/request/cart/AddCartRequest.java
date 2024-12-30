package ecommerce.coupang.dto.request.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCartRequest {

	@NotNull(message = "상품을 선택해주세요.")
	private final Long productVariantId;

	@Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
	private final int quantity;
}
