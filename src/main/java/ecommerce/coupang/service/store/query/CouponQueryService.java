package ecommerce.coupang.service.store.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.dto.request.store.coupon.CouponSort;
import ecommerce.coupang.dto.response.store.coupon.CouponDetailResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.store.CouponProductRepository;
import ecommerce.coupang.repository.store.CouponRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponQueryService {

	private final CouponRepository couponRepository;
	private final CouponProductRepository couponProductRepository;

	/**
	 * 나의 쿠폰 목록 조회
	 * @param member 요청한 회원
	 * @param page 현재 페이지
	 * @param pageSize 페이지당 쿠폰 개수
	 * @param sort 정렬타입
	 * @return 쿠폰 목록
	 */
	@LogAction("나의 쿠폰 목록 조회")
	public Page<MemberCoupon> findMyCoupons(Member member, int page, int pageSize, CouponSort sort) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return couponRepository.findMyCoupons(member.getId(), pageable, sort);
	}

	/**
	 * 상점 쿠폰 목록 조회
	 * @param storeId 상점 ID
	 * @param page 현재 페이지
	 * @param pageSize 페이지당 쿠폰 개수
	 * @param sort 정렬 타입
	 * @return 쿠폰 목록
	 */
	@LogAction("상점 쿠폰 목록 조회")
	public Page<Coupon> findCouponsByStore(Long storeId, int page, int pageSize, CouponSort sort) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return couponRepository.findCouponsByStore(storeId, pageable, sort);
	}
	/**
	 * 상품 쿠폰 조회
	 * @param productId 상품 ID
	 * @param page 현재 페이지
	 * @param pageSize 페이지당 쿠폰 개수
	 * @param sort 정렬 타입
	 * @return 쿠폰 목록
	 */
	@LogAction("상품 쿠폰 목록 조회")
	public Page<CouponProduct> findCouponsByProduct(Long productId, int page, int pageSize, CouponSort sort) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return couponRepository.finCouponsByProduct(productId, pageable, sort);
	}
	/**
	 * 쿠폰 상세 조회
	 * @param couponId 쿠폰 ID
	 * @return 쿠폰 상세 정보
	 * @throws CustomException
	 */
	@LogAction("쿠폰 상세 조회")
	public CouponDetailResponse findCoupon(Long couponId) throws CustomException {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		List<CouponProduct> couponProducts = couponProductRepository.findByCouponId(couponId);

		return CouponDetailResponse.from(coupon, couponProducts);
	}
}
