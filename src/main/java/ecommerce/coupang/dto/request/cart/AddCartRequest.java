package ecommerce.coupang.dto.request.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCartRequest {

	private final Long productVariantId;
	private final int quantity;
}
