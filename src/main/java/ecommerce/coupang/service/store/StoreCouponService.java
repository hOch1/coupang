package ecommerce.coupang.service.store;

import org.springframework.data.domain.Page;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.exception.CustomException;

public interface StoreCouponService {


	Coupon createCoupon(Long storeId, CreateCouponRequest request, Member member) throws CustomException;

	Coupon downloadCoupon(Long couponId, Member member) throws CustomException;

	Page<MemberCoupon> findMyCoupons(Member member);

	Page<Coupon> findCouponsByStore(Long storeId, Member member);

	Coupon findCoupon(Long couponId) throws CustomException;
}
