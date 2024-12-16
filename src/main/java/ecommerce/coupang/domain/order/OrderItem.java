package ecommerce.coupang.domain.order;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.exception.CustomException;
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
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "total_price", nullable = false)
	private int totalPrice;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	public OrderItem(Order order, Product product, int price, int quantity, int totalPrice) {
		this.order = order;
		this.product = product;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public static OrderItem create(Order order, ProductDetail productDetail, CreateOrderRequest request) throws CustomException {
		productDetail.order(request.getQuantity());

		return new OrderItem(
			order,
			productDetail.getProduct(),
			productDetail.getPrice(),
			request.getQuantity(),
			productDetail.getPrice() * request.getQuantity()
		);
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
}
