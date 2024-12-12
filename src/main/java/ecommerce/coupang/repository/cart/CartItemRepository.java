package ecommerce.coupang.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.cart.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
