package ecommerce.coupang.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.member.Member;

@LogLevel("CartRepository")
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByMember(Member member);
}
