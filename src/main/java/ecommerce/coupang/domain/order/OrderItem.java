package ecommerce.coupang.domain.order;

import java.util.Objects;

import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "total_price", nullable = false)
	private int totalPrice;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private Delivery delivery;

	public OrderItem(Order order, ProductVariant productVariant, int price, int quantity, int totalPrice) {
		this.order = order;
		this.productVariant = productVariant;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public static OrderItem createByProduct(Order order, ProductVariant productVariant, CreateOrderByProductRequest request) {
		return new OrderItem(
			order,
			productVariant,
			productVariant.getPrice(),
			request.getQuantity(),
			productVariant.getPrice() * request.getQuantity()
		);
	}

	public static OrderItem createByCartItem(Order order, CartItem cartItem) {
		return new OrderItem(
			order,
			cartItem.getProductVariant(),
			cartItem.getProductVariant().getPrice(),
			cartItem.getQuantity(),
			cartItem.getProductVariant().getPrice() * cartItem.getQuantity()
		);
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public void cancel() {
		productVariant.addStock(quantity);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		OrderItem orderItem = (OrderItem)o;
		return Objects.equals(id, orderItem.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
