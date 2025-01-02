package ecommerce.coupang.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.store.CouponProduct;

public interface CouponProductRepository extends JpaRepository<CouponProduct, Long> {

	@Query("select cp from CouponProduct cp "
		+ "join fetch cp.coupon c "
		+ "where cp.product.id = :productId ")
	List<CouponProduct> findByProductId(Long productId);

	@Query("select cp from CouponProduct cp "
		+ "join fetch cp.product p "
		+ "where cp.coupon.id = :couponId ")
	List<CouponProduct> findByCouponId(Long couponId);
}
