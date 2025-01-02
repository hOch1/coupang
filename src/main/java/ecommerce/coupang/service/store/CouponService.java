package ecommerce.coupang.service.store;

import java.util.List;

import org.springframework.data.domain.Page;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.dto.request.store.coupon.CouponSort;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.dto.response.store.coupon.CouponDetailResponse;
import ecommerce.coupang.exception.CustomException;

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

	/**
	 * 나의 쿠폰 목록 조회
	 * @param member 요청한 회원
	 * @param page 현재 페이지
	 * @param pageSize 페이지당 쿠폰 개수
	 * @param sort 정렬타입
	 * @return 쿠폰 목록
	 */
	@LogAction("나의 쿠폰 목록 조회")
	Page<MemberCoupon> findMyCoupons(Member member, int page, int pageSize, CouponSort sort);

	/**
	 * 상점 쿠폰 목록 조회
	 * @param storeId 상점 ID
	 * @param page 현재 페이지
	 * @param pageSize 페이지당 쿠폰 개수
	 * @param sort 정렬 타입
	 * @return 쿠폰 목록
	 */
	@LogAction("상점 쿠폰 목록 조회")
	Page<Coupon> findCouponsByStore(Long storeId, int page, int pageSize, CouponSort sort);

	/**
	 * 상품 쿠폰 조회
	 * @param productId 상품 ID
	 * @param page 현재 페이지
	 * @param pageSize 페이지당 쿠폰 개수
	 * @param sort 정렬 타입
	 * @return 쿠폰 목록
	 */
	@LogAction("상품 쿠폰 목록 조회")
	Page<CouponProduct> findCouponsByProduct(Long productId, int page, int pageSize, CouponSort sort);

	/**
	 * 쿠폰 상세 조회
	 * @param couponId 쿠폰 ID
	 * @return 쿠폰 상세 정보
	 * @throws CustomException
	 */
	@LogAction("쿠폰 상세 조회")
	CouponDetailResponse findCoupon(Long couponId) throws CustomException;


}
