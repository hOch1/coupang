package ecommerce.coupang.repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.cart.CartItem;

@LogLevel("CartItemRepository")
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	/**
	 * ID로 장바구니 상품 조회
	 * @param cartItemId 장바구니 상품 ID
	 * @return 장바구니 상품
	 */
	@Query("select ci from CartItem ci "
		+ "join fetch ci.cart c "
		+ "join fetch c.member m "
		+ "where ci.id = :id ")
	Optional<CartItem> findByIdWithMember(Long cartItemId);

	/**
	 * ID로 장바구니 상품 조회
	 * @param cartItemId 장바구니 상품 ID
	 * @return 장바구니 상품
	 */
	@Query("select ci from CartItem ci "
		+ "join fetch ci.productVariant pv "
		+ "join fetch pv.product p "
		+ "join fetch p.store s "
		+ "where ci.id = :id ")
	Optional<CartItem> findByIdWithStore(Long cartItemId);

	/**
	 * 장바구니 ID, 상품 변형 ID로 장바구니 상품 조회
	 * @param cartId 장바구니 ID
	 * @param productVariantId 상품 변형 ID
	 * @return 장바구니 상품
	 */
	Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);
}
