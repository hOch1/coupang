package ecommerce.coupang.service.store.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.coupon.CouponSort;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.dto.response.store.coupon.CouponDetailResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.MemberCouponRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.store.CouponProductRepository;
import ecommerce.coupang.repository.store.CouponRepository;
import ecommerce.coupang.repository.store.StoreRepository;
import ecommerce.coupang.service.store.CouponService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;
	private final StoreRepository storeRepository;
	private final MemberCouponRepository memberCouponRepository;
	private final ProductRepository productRepository;
	private final CouponProductRepository couponProductRepository;

	@Override
	@Transactional
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
	@Transactional
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

	@Override
	public Page<MemberCoupon> findMyCoupons(Member member, int page, int pageSize, CouponSort sort) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return couponRepository.findMyCoupons(member.getId(), pageable, sort);
	}

	@Override
	public Page<Coupon> findCouponsByStore(Long storeId, int page, int pageSize, CouponSort sort) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return couponRepository.findCouponsByStore(storeId, pageable, sort);
	}

	@Override
	public Page<CouponProduct> findCouponsByProduct(Long productId, int page, int pageSize, CouponSort sort) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return couponRepository.finCouponsByProduct(productId, pageable, sort);
	}

	@Override
	public CouponDetailResponse findCoupon(Long couponId) throws CustomException {
		Coupon coupon = couponRepository.findById(couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

		List<CouponProduct> couponProducts = couponProductRepository.findByCouponId(couponId);

		return CouponDetailResponse.from(coupon, couponProducts);
	}

}
