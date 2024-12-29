package ecommerce.coupang.dto.response.cart;

import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.response.option.OptionResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartResponse {

	private Long cartId;
	private int itemCount;
	private List<CartItemResponse> items;

	public CartResponse(Long cartId, int itemCount) {
		this.cartId = cartId;
		this.itemCount = itemCount;
		this.items = new ArrayList<>();
	}

	public static CartResponse from(Cart cart) {
		return new CartResponse(
			cart.getId(),
			cart.getCartItems().size()
		);
	}

	public void addItems(CartItemResponse cartItemResponse) {
		this.items.add(cartItemResponse);
	}

	@Getter
	@AllArgsConstructor
	public static class CartItemResponse {

		private final Long cartItemId;
		private final StoreResponse store;
		private final Long productVariantId;
		private final String name;
		private final int price;
		private final int quantity;
		private final ProductStatus status;
		private final List<OptionResponse> variantOptions;

		public static CartItemResponse from(CartItem cartItem, List<ProductVariantOption> productVariantOptions) {
			return new CartItemResponse(
				cartItem.getId(),
				StoreResponse.from(cartItem.getProductVariant().getProduct().getStore()),
				cartItem.getProductVariant().getId(),
				cartItem.getProductVariant().getProduct().getName(),
				cartItem.getProductVariant().getPrice(),
				cartItem.getQuantity(),
				cartItem.getProductVariant().getStatus(),
				productVariantOptions.stream()
					.map(OptionResponse::productVariantFrom)
					.toList()
			);
		}
	}
}
