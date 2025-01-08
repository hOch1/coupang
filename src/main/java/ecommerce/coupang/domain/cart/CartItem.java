package ecommerce.coupang.domain.cart;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
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
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	public CartItem(ProductVariant productVariant, Cart cart, int quantity) {
		this.productVariant = productVariant;
		this.cart = cart;
		this.quantity = quantity;
	}

	public static CartItem create(Cart cart, ProductVariant productVariant, int quantity) {
		return new CartItem(
			productVariant,
			cart,
			quantity
		);
	}

	/**
	 * 장바구니 상품 수량 변경
	 * 차감시 0 보다 작은지 체크
	 * @param quantity 변경 수량
	 */
	public void changeQuantity(int quantity) throws CustomException {
		if (this.quantity - quantity <= 0)
			throw new CustomException(ErrorCode.QUANTITY_IS_WRONG);
		this.quantity = quantity;
	}

	/**
	 * 장바구니 상품 수량 추가
	 * @param quantity 추가할 수량
	 */
	public void addQuantity(int quantity) {
		this.quantity += quantity;
	}
}
