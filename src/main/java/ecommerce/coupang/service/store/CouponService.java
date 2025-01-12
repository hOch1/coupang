package ecommerce.coupang.service.store;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.service.store.query.StoreQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.member.MemberCouponRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.store.CouponRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@LogLevel("CouponService")
public class CouponService {

	private final CouponRepository couponRepository;
	private final StoreQueryService storeQueryService;
	private final MemberCouponRepository memberCouponRepository;
	private final ProductRepository productRepository;

	/**
	 * 쿠폰 생성
	 * @param storeId 상점 ID
	 * @param request 쿠폰 생성 요청 정보
	 * @param member 요청한 회원
	 * @return 쿠폰
	 */
	@LogAction("쿠폰 생성")
	public Coupon createCoupon(Long storeId, CreateCouponRequest request, Member member) throws CustomException {
		Store store = storeQueryService.findStore(storeId);
		store.validateOwner(member);

		Coupon coupon = Coupon.of(request, store);

		for (Long productVariantId : request.getProductIds()) {
			Product product = productRepository.findById(productVariantId)
				.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

			CouponProduct couponProduct = CouponProduct.of(coupon, product);
			coupon.addCouponProducts(couponProduct);
			product.addCouponProducts(couponProduct);
		}

		couponRepository.save(coupon);
		return coupon;
	}

	/**
	 * 쿠폰 다운로드
	 * @param couponId 쿠폰 ID
	 * @param member 요청한 회원
	 * @return 쿠폰
	 */
	@LogAction("쿠폰 다운로드")
	public Coupon downloadCoupon(Long couponId, Member member) throws CustomException {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		if (memberCouponRepository.existsByCouponId(couponId))
			throw new CustomException(ErrorCode.ALREADY_HAS_COUPON);

		MemberCoupon memberCoupon = MemberCoupon.of(member, coupon);
		memberCouponRepository.save(memberCoupon);

		coupon.reduceStock();
		return coupon;
	}

	public MemberCoupon getMemberCoupon(Member member, Long couponId) throws CustomException {
		return memberCouponRepository.findByMemberIdAndCouponId(member.getId(), couponId)
				.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
	}
}
