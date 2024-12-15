package ecommerce.coupang.dto.request.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCartRequest {

	private final Long productDetailId;
	private final int quantity;

}
