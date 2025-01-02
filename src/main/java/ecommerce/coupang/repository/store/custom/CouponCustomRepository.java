package ecommerce.coupang.repository.store.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.dto.request.store.coupon.CouponSort;

public interface CouponCustomRepository {

	Page<MemberCoupon> findMyCoupons(Long memberId, Pageable pageable, CouponSort sort);

	Page<CouponProduct> finCouponsByProduct(Long productId, Pageable pageable, CouponSort sort);

	Page<Coupon> findCouponsByStore(Long storeId, Pageable pageable, CouponSort sort);
}
