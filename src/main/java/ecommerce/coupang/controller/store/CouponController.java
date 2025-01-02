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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.dto.request.store.coupon.CouponSort;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.store.coupon.CouponDetailResponse;
import ecommerce.coupang.dto.response.store.coupon.CouponResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.store.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "쿠폰 API V1", description = "쿠폰 관련 API")
public class CouponController {

	private final CouponService couponService;

	@PostMapping("/{storeId}/store")
	@Operation(summary = "쿠폰 생성 API", description = "쿠폰을 생성합니다.")
	public ResponseEntity<Void> createCoupon(
		@PathVariable Long storeId,
		@RequestBody CreateCouponRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		couponService.createCoupon(storeId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/{couponId}/member")
	@Operation(summary = "쿠폰 다운로드 API", description = "해당 쿠폰을 받습니다.")
	public ResponseEntity<Void> downloadCoupon(
		@PathVariable Long couponId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		couponService.downloadCoupon(couponId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/me")
	@Operation(summary = "나의 쿠폰 조회 API", description = "나의 쿠폰을 조회합니다")
	public ResponseEntity<Result<List<CouponResponse>>> getMyCoupons(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(required = false, defaultValue = "LATEST") CouponSort sort,
		@RequestParam(required = false, defaultValue = "0") int page,
		@RequestParam(required = false, defaultValue = "10") int pageSize) {

		Page<MemberCoupon> coupons = couponService.findMyCoupons(userDetails.getMember(), page, pageSize, sort);
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

	@GetMapping("/{storeId}/store")
	@Operation(summary = "상점 쿠폰 조회 API", description = "상점에서 발행한 쿠폰목록을 조회합니다")
	public ResponseEntity<Result<List<CouponResponse>>> getCouponsByStore(
		@PathVariable Long storeId,
		@RequestParam(required = false, defaultValue = "LATEST") CouponSort sort,
		@RequestParam(required = false, defaultValue = "0") int page,
		@RequestParam(required = false, defaultValue = "10") int pageSize) {

		Page<Coupon> coupons = couponService.findCouponsByStore(storeId, page, pageSize, sort);
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

	@GetMapping("/{productId}/product")
	@Operation(summary = "상품 쿠폰 조회 API", description = "해당 상품의 쿠폰목록 조회")
	public ResponseEntity<Result<List<CouponResponse>>> getCouponByProduct(
		@PathVariable Long productId,
		@RequestParam(required = false, defaultValue = "LATEST") CouponSort sort,
		@RequestParam(required = false, defaultValue = "0") int page,
		@RequestParam(required = false, defaultValue = "10") int pageSize) {

		Page<CouponProduct> coupons = couponService.findCouponsByProduct(productId, page, pageSize, sort);
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
	@Operation(summary = "쿠폰 상세조회 API", description = "해당 쿠폰을 상세 조회합니다.")
	public ResponseEntity<Result<CouponDetailResponse>> getCoupon(
		@PathVariable Long couponId) throws CustomException {

		CouponDetailResponse response = couponService.findCoupon(couponId);
		return ResponseEntity.ok(new Result<>(response));
	}
}