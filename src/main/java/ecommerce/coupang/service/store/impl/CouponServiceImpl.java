package ecommerce.coupang.service.store.impl;

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
import ecommerce.coupang.repository.store.StoreRepository;
import ecommerce.coupang.service.store.CouponService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;
	private final StoreRepository storeRepository;
	private final MemberCouponRepository memberCouponRepository;
	private final ProductRepository productRepository;

	@Override
	public Coupon createCoupon(Long storeId, CreateCouponRequest request, Member member) throws CustomException {
		Store store = storeRepository.findByIdWithMember(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		Coupon coupon = Coupon.create(request, store);

		for (Long productVariantId : request.getProductIds()) {
			Product product = productRepository.findById(productVariantId)
				.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

			CouponProduct couponProduct = CouponProduct.create(coupon, product);
			coupon.addCouponProducts(couponProduct);
			product.addCouponProducts(couponProduct);
		}

		couponRepository.save(coupon);
		return coupon;
	}

	@Override
	public Coupon downloadCoupon(Long couponId, Member member) throws CustomException {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		if (memberCouponRepository.existsByCouponId(couponId))
			throw new CustomException(ErrorCode.ALREADY_HAS_COUPON);

		MemberCoupon memberCoupon = MemberCoupon.create(member, coupon);
		memberCouponRepository.save(memberCoupon);

		coupon.reduceStock();
		return coupon;
	}
}
