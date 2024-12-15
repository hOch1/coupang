package ecommerce.coupang.domain.cart;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
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
	@JoinColumn(name = "product_detail_id", nullable = false)
	private ProductDetail productDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	public CartItem(ProductDetail productDetail, Cart cart, int quantity) {
		this.productDetail = productDetail;
		this.cart = cart;
		this.quantity = quantity;
	}

	public static CartItem create(Cart cart, ProductDetail productDetail, int quantity) {
		return new CartItem(
			productDetail,
			cart,
			quantity
		);
	}

	public void changeQuantity(int quantity) throws CustomException {
		if (quantity <= 0)
			throw new CustomException(ErrorCode.QUANTITY_IS_WRONG);
		this.quantity = quantity;
	}

	public void addQuantity() {
		this.quantity++;
	}

	public void subQuantity() throws CustomException {
		if (this.quantity <= 1)
			throw new CustomException(ErrorCode.QUANTITY_IS_WRONG);

		this.quantity--;
	}
}
