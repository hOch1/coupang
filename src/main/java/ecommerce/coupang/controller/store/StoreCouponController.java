package ecommerce.coupang.controller.store;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.member.MemberCouponResponse;
import ecommerce.coupang.dto.response.store.coupon.CouponDetailResponse;
import ecommerce.coupang.dto.response.store.coupon.CouponResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.store.StoreCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "쿠폰 API V1", description = "쿠폰 관련 API")
public class StoreCouponController {

	private final StoreCouponService storeCouponService;

	@PostMapping("/{storeId}")
	@Operation(summary = "쿠폰 생성 API", description = "쿠폰을 생성합니다.")
	public ResponseEntity<Void> createCoupon(
		@PathVariable Long storeId,
		@RequestBody CreateCouponRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeCouponService.createCoupon(storeId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/{couponId}")
	@Operation(summary = "쿠폰 다운로드 API", description = "해당 쿠폰을 받습니다.")
	public ResponseEntity<Void> downloadCoupon(
		@PathVariable Long couponId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeCouponService.downloadCoupon(couponId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/me")
	@Operation(summary = "나의 쿠폰 조회 API", description = "나의 쿠폰을 조회합니다")
	public ResponseEntity<Result<List<MemberCouponResponse>>> getMyCoupons(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Page<MemberCoupon> coupons = storeCouponService.findMyCoupons(userDetails.getMember());
		Page<MemberCouponResponse> responses = coupons.map(MemberCouponResponse::from);
		return ResponseEntity.ok(new Result<>(
			responses.getContent(),
			responses.getContent().size(),
			responses.getNumber(),
			responses.getSize(),
			responses.getTotalPages(),
			responses.getTotalElements()
		));
	}

	@GetMapping("/{storeId}/store")
	@Operation(summary = "상점 쿠폰 조회 API", description = "상점에서 발행한 쿠폰목록을 조회합니다")
	public ResponseEntity<Result<List<CouponResponse>>> getCouponsByStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		Page<Coupon> coupons = storeCouponService.findCouponsByStore(storeId, userDetails.getMember());
		Page<CouponResponse> responses = coupons.map(CouponResponse::from);
		return ResponseEntity.ok(new Result<>(
			responses.getContent(),
			responses.getContent().size(),
			responses.getNumber(),
			responses.getSize(),
			responses.getTotalPages(),
			responses.getTotalElements()
		));
	}

	@GetMapping("/{couponId}")
	public ResponseEntity<Result<CouponDetailResponse>> getCoupon(
		@PathVariable Long couponId) throws CustomException {

		Coupon coupon = storeCouponService.findCoupon(couponId);
		CouponDetailResponse response = CouponDetailResponse.from(coupon);
		return ResponseEntity.ok(new Result<>(response));
	}
}
