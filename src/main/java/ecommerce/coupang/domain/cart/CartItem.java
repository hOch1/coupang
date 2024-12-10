package ecommerce.coupang.domain.cart;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CartItem extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	public CartItem(Product product, Cart cart, int quantity) {
		this.product = product;
		this.cart = cart;
		this.quantity = quantity;
	}

	public static CartItem create(Cart cart, Product product, int quantity) {
		return new CartItem(
			product,
			cart,
			quantity
		);
	}
}
