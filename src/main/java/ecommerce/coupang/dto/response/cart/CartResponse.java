package ecommerce.coupang.dto.response.cart;

import java.util.List;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.dto.response.option.OptionResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponse {

	private final Long cartId;
	private final int itemCount;
	private final List<CartItemResponse> items;

	public static CartResponse from(Cart cart) {
		return new CartResponse(
			cart.getId(),
			cart.getCartItems().size(),
			cart.getCartItems().stream()
				.map(CartItemResponse::from)
				.toList()
		);
	}

	@Getter
	@AllArgsConstructor
	private static class CartItemResponse {

		private final Long cartItemId;
		private final StoreResponse store;
		private final Long productVariantId;
		private final String name;
		private final int price;
		private final int quantity;
		private final ProductStatus status;
		private final List<OptionResponse> categoryOptions;
		private final List<OptionResponse> variantOptions;

		public static CartItemResponse from(CartItem cartItem) {
			return new CartItemResponse(
				cartItem.getId(),
				StoreResponse.from(cartItem.getProductVariant().getProduct().getStore()),
				cartItem.getProductVariant().getId(),
				cartItem.getProductVariant().getProduct().getName(),
				cartItem.getProductVariant().getPrice(),
				cartItem.getQuantity(),
				cartItem.getProductVariant().getStatus(),
				cartItem.getProductVariant().getProduct().getProductOptions().stream()
					.map(OptionResponse::categoryOptionFrom)
					.toList(),
				cartItem.getProductVariant().getProductVariantOption().stream()
					.map(OptionResponse::productVariantFrom)
					.toList()
			);
		}
	}
}
