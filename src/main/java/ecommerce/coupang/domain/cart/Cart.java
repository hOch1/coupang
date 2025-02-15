package ecommerce.coupang.domain.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ecommerce.coupang.domain.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> cartItems = new ArrayList<>();

	public Cart(Member member) {
		this.member = member;
	}

	public static Cart from(Member member) {
		return new Cart(member);
	}

	/* 장바구니 상품 추가 */
	public void addItem(CartItem cartItem) {
		this.cartItems.add(cartItem);
	}

	/* 장바구니 상품 제거 */
	public void removeItem(CartItem cartItem) {
		this.cartItems.remove(cartItem);
	}

	/* 장바구니 전체 제거 */
	public void clear() {
		this.cartItems.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cart cart = (Cart)o;
		return Objects.equals(id, cart.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
