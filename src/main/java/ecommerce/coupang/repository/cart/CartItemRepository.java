package ecommerce.coupang.repository.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.product.variant.ProductVariant;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	@Query("select ci from CartItem ci "
		+ "join fetch ci.cart c "
		+ "join fetch c.member m "
		+ "where ci.id = :id ")
	Optional<CartItem> findByIdWithMember(Long id);

	@Query("select ci from CartItem ci "
		+ "join fetch ci.productVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "where ci.id = :id ")
	Optional<CartItem> findByIdWithStore(Long id);

	Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);
}
