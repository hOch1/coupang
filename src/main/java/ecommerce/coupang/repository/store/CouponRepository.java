package ecommerce.coupang.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.repository.store.custom.CouponCustomRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponCustomRepository {
}
