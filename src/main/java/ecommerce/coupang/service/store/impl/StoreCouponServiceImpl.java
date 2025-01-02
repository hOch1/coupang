package ecommerce.coupang.service.store.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.MemberCouponRepository;
import ecommerce.coupang.repository.store.StoreCouponRepository;
import ecommerce.coupang.repository.store.StoreRepository;
import ecommerce.coupang.service.store.StoreCouponService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreCouponServiceImpl implements StoreCouponService {

	private final StoreCouponRepository storeCouponRepository;
	private final StoreRepository storeRepository;
	private final MemberCouponRepository memberCouponRepository;

	@Override
	@Transactional
	public Coupon createCoupon(Long storeId, CreateCouponRequest request, Member member) throws CustomException {
		Store store = storeRepository.findByIdWithMember(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		Coupon coupon = Coupon.create(request, store);

		storeCouponRepository.save(coupon);
		return coupon;
	}

	@Override
	@Transactional
	public Coupon downloadCoupon(Long couponId, Member member) throws CustomException {
		Coupon coupon = findCoupon(couponId);

		if (memberCouponRepository.existsByStoreCouponId(couponId))
			throw new CustomException(ErrorCode.ALREADY_HAS_COUPON);

		MemberCoupon memberCoupon = MemberCoupon.create(member, coupon);
		memberCouponRepository.save(memberCoupon);

		coupon.reduceStock();

		return coupon;
	}

	@Override
	public Page<MemberCoupon> findMyCoupons(Member member) {
		List<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberId(member.getId());
		return null;
	}

	@Override
	public Page<Coupon> findCouponsByStore(Long storeId, Member member) {
		return null;
	}

	@Override
	public Coupon findCoupon(Long couponId) throws CustomException {
		return storeCouponRepository.findById(couponId)
			.orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
	}
}
