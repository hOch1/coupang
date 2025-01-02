package ecommerce.coupang.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.store.Coupon;

public interface StoreCouponRepository extends JpaRepository<Coupon, Long> {
}
