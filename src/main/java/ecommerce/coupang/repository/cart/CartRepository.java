package ecommerce.coupang.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.cart.Cart;

@LogLevel("CartRepository")
public interface CartRepository extends JpaRepository<Cart, Long> {

	/**
	 * 회원 ID로 장바구니 조회
	 * @param memberId 회원
	 * @return 장바구니
	 */
	Optional<Cart> findByMemberId(Long memberId);
}
