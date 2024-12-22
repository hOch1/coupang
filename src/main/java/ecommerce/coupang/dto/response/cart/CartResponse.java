package ecommerce.coupang.dto.response.cart;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.dto.response.member.MemberResponse;
import ecommerce.coupang.dto.response.product.OptionResponse;
import ecommerce.coupang.dto.response.store.StoreDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponse {

	private final Long cartId;
	private final MemberResponse member;
	// private final List<CartItemResponse> items;


	public static CartResponse from(Cart cart) {
		return new CartResponse(
			cart.getId(),
			MemberResponse.from(cart.getMember())
			// cart.getCartItems().stream()
			// 	.map(CartItemResponse::from)
			// 	.toList()
		);
	}

	@Getter
	@AllArgsConstructor
	private static class CartItemResponse {

		private final Long cartItemId;
		private final StoreDetailResponse store;
		private final String name;
		private final int price;
		private final int quantity;
		private final OptionResponse option;
		private final ProductStatus status;

		// public static CartItemResponse from(CartItem cartItem) {
		// 	return new CartItemResponse(
		// 		cartItem.getId(),
		// 		StoreResponse.from(cartItem.getProduct().getStore()),
		// 		cartItem.getProduct().getName(),
		// 		cartItem.getProduct().getP(),
		// 		cartItem.getQuantity(),
		// 		cartItem.getProduct().getStatus()
		// 	);
		// }
	}
}
