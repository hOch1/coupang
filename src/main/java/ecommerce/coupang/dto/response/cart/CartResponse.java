package ecommerce.coupang.dto.response.cart;

import java.util.List;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.product.ProductStatus;
import ecommerce.coupang.dto.response.member.MemberResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponse {

	private final Long cartId;
	private final MemberResponse member;
	private final List<CartItemResponse> items;


	public static CartResponse from(Cart cart) {
		return new CartResponse(
			cart.getId(),
			MemberResponse.from(cart.getMember()),
			cart.getCartItems().stream().map(CartItemResponse::from).toList()
		);
	}

	@Getter
	@AllArgsConstructor
	public static class CartItemResponse {

		private final Long cartItemId;
		private final StoreResponse store;
		private final String name;
		private final int price;
		private final int quantity;
		private final ProductStatus status;

		public static CartItemResponse from(CartItem cartItem) {
			return new CartItemResponse(
				cartItem.getId(),
				StoreResponse.from(cartItem.getProduct().getStore()),
				cartItem.getProduct().getName(),
				cartItem.getProduct().getPrice(),
				cartItem.getQuantity(),
				cartItem.getProduct().getStatus()
			);
		}
	}
}
