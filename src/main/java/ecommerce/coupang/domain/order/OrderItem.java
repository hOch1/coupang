package ecommerce.coupang.domain.order;

import java.util.Objects;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Setter;

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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_coupon_id")
	private MemberCoupon memberCoupon;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "discount_price", nullable = false)
	private int discountPrice;

	@Column(name = "total_price", nullable = false)
	private int totalPrice;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_item_status", nullable = false)
	private OrderItemStatus status = OrderItemStatus.ORDERED;

	@Setter
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private Delivery delivery;

	public OrderItem(Order order, ProductVariant productVariant, MemberCoupon memberCoupon, int price, int quantity, int discountPrice, int totalPrice) {
		this.order = order;
		this.productVariant = productVariant;
		this.memberCoupon = memberCoupon;
		this.price = price;
		this.quantity = quantity;
		this.discountPrice = discountPrice;
		this.totalPrice = totalPrice;
	}

	public static OrderItem of(Order order, ProductVariant productVariant, MemberCoupon memberCoupon, int quantity, int discountPrice) {
		return new OrderItem(order,
			productVariant,
			memberCoupon,
			productVariant.getPrice(),
			quantity,
			discountPrice,
			productVariant.getPrice() * quantity - discountPrice
		);
	}

	/* 주문 상품 취소 */
	public void cancel() throws CustomException {
		this.delivery.cancel();
		this.status = OrderItemStatus.CANCEL;
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
