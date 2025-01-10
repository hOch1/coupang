package ecommerce.coupang.dto.request.order;

import java.util.List;

import ecommerce.coupang.domain.order.Payment;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class OrderByCartRequest extends CreateOrderRequest{

	@NotEmpty(message = "상품을 하나이상 선택해주세요.")
	private final List<CartItemRequest> cartItems;

	public OrderByCartRequest(Long addressId, Payment payment, String orderMessage, List<CartItemRequest> cartItems) {
		super(addressId, payment, orderMessage);
		this.cartItems = cartItems;
	}

	@Getter
	@AllArgsConstructor
	public static class CartItemRequest {
		private final Long cartItemId;
		private final Long couponId;
	}
}
