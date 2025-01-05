package ecommerce.coupang.service.store;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.common.exception.CustomException;

@LogLevel("CouponService")
public interface CouponService {

	/**
	 * 쿠폰 생성
	 * @param storeId 상점 ID
	 * @param request 쿠폰 생성 요청 정보
	 * @param member 요청한 회원
	 * @return 쿠폰
	 * @throws CustomException
	 */
	@LogAction("쿠폰 생성")
	Coupon createCoupon(Long storeId, CreateCouponRequest request, Member member) throws CustomException;

	/**
	 * 쿠폰 다운로드
	 * @param couponId 쿠폰 ID
	 * @param member 요청한 회원
	 * @return 쿠폰
	 * @throws CustomException
	 */
	@LogAction("쿠폰 다운로드")
	Coupon downloadCoupon(Long couponId, Member member) throws CustomException;

}
