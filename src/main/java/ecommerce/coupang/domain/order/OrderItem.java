package ecommerce.coupang.domain.order;

import java.util.Objects;

import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.store.Coupon;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id")
	private Coupon coupon;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "coupon_discount_price", nullable = false)
	private int couponDiscountPrice;

	@Column(name = "memberDiscountPrice", nullable = false)
	private int memberDiscountPrice;

	@Column(name = "total_price", nullable = false)
	private int totalPrice;

	@Column(name = "discount_total_price", nullable = false)
	private int discountTotalPrice;

	@Setter
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private Delivery delivery;

	public OrderItem(Order order, ProductVariant productVariant, Coupon coupon, int price, int quantity, int couponDiscountPrice, int memberDiscountPrice, int totalPrice, int discountTotalPrice) {
		this.order = order;
		this.productVariant = productVariant;
		this.coupon = coupon;
		this.price = price;
		this.quantity = quantity;
		this.couponDiscountPrice = couponDiscountPrice;
		this.memberDiscountPrice = memberDiscountPrice;
		this.totalPrice = totalPrice;
		this.discountTotalPrice = discountTotalPrice;
	}

	public static OrderItem createByProduct(Order order, ProductVariant productVariant, MemberCoupon memberCoupon, CreateOrderByProductRequest request, int couponDiscountPrice, int memberDiscountPrice) {
		productVariant.increaseSalesCount(request.getQuantity());
		Coupon coupon = memberCoupon != null ? memberCoupon.getCoupon() : null;
		return new OrderItem(
			order,
			productVariant,
			coupon,
			productVariant.getPrice(),
			request.getQuantity(),
			couponDiscountPrice,
			memberDiscountPrice,
			productVariant.getPrice() * request.getQuantity(),
			productVariant.getPrice() * request.getQuantity() - couponDiscountPrice - memberDiscountPrice
		);
	}

	public static OrderItem createByCartItem(Order order, CartItem cartItem, MemberCoupon memberCoupon, int couponDiscountPrice, int memberDiscountPrice) {
		cartItem.getProductVariant().increaseSalesCount(cartItem.getQuantity());
		Coupon coupon = memberCoupon != null ? memberCoupon.getCoupon() : null;
		return new OrderItem(
			order,
			cartItem.getProductVariant(),
			coupon,
			cartItem.getProductVariant().getPrice(),
			cartItem.getQuantity(),
			couponDiscountPrice,
			memberDiscountPrice,
			cartItem.getProductVariant().getPrice() * cartItem.getQuantity(),
			cartItem.getProductVariant().getPrice() * cartItem.getQuantity() - couponDiscountPrice - memberDiscountPrice
		);
	}

	public void cancel() {
		productVariant.addStock(quantity);
		productVariant.decreaseSalesCount(quantity);
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
