package ecommerce.coupang.service.store;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.domain.store.CouponType;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import ecommerce.coupang.repository.member.MemberCouponRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.store.CouponRepository;
import ecommerce.coupang.service.store.query.StoreQueryService;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

	@InjectMocks
	private CouponService couponService;

	@Mock
	private CouponRepository couponRepository;

	@Mock
	private StoreQueryService storeQueryService;

	@Mock
	private MemberCouponRepository memberCouponRepository;

	@Mock
	private ProductRepository productRepository;

	private Store mockStore = mock(Store.class);
	private Product mockProduct = mock(Product.class);
	private Member mockMember = mock(Member.class);
	private Coupon mockCoupon = mock(Coupon.class);

	@Test
	@DisplayName("쿠폰 생성 테스트")
	void createCoupon() throws CustomException {
		CreateCouponRequest request = createCouponRequest();
		Long storeId = 1L;

		when(mockStore.getMember()).thenReturn(mockMember);
		when(storeQueryService.findStore(storeId)).thenReturn(mockStore);
		when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

		Coupon coupon = couponService.createCoupon(storeId, request, mockMember);

		verify(couponRepository).save(any(Coupon.class));
		verify(storeQueryService).findStore(storeId);
		verify(productRepository).findById(1L);
		verify(mockProduct).addCouponProducts(any(CouponProduct.class));

		assertThat(coupon).isNotNull();
	}

	@Test
	@DisplayName("쿠폰 생성 테스트 - 실패 (상품 못찾음)")
	void createCoupon_fail_storeNotFound() throws CustomException {
		CreateCouponRequest request = createCouponRequest();
		Long storeId = 1L;

		when(mockStore.getMember()).thenReturn(mockMember);
		when(storeQueryService.findStore(storeId)).thenReturn(mockStore);
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> couponService.createCoupon(storeId, request, mockMember));

		verify(couponRepository, never()).save(any(Coupon.class));
		verify(storeQueryService).findStore(storeId);
		verify(productRepository).findById(1L);

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	@Test
	@DisplayName("쿠폰 다운로드 테스트")
	void downloadCoupon() throws CustomException {
		Long couponId = 1L;

		when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
		when(memberCouponRepository.existsByCouponId(couponId)).thenReturn(false);

		Coupon coupon = couponService.downloadCoupon(couponId, mockMember);

		verify(memberCouponRepository).save(any(MemberCoupon.class));
		verify(couponRepository).findById(couponId);
		verify(memberCouponRepository).existsByCouponId(couponId);
		verify(mockCoupon).reduceStock();

		assertThat(coupon).isNotNull();
	}

	@Test
	@DisplayName("쿠폰 다운로드 테스트 - 실패 (쿠폰 못찾음)")
	void downloadCoupon_fail_couponNotFound() {
		Long couponId = 1L;

		when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> couponService.downloadCoupon(couponId, mockMember));

		verify(memberCouponRepository, never()).save(any(MemberCoupon.class));
		verify(couponRepository).findById(couponId);
		verify(memberCouponRepository, never()).existsByCouponId(couponId);
		verify(mockCoupon, never()).reduceStock();

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.COUPON_NOT_FOUND);
	}

	@Test
	@DisplayName("쿠폰 다운로드 테스트 - 실패 (이미 다운받은 쿠폰)")
	void downloadCoupon_fail_alreadyHasCoupon() {
		Long couponId = 1L;

		when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
		when(memberCouponRepository.existsByCouponId(couponId)).thenReturn(true);

		CustomException customException = assertThrows(CustomException.class,
			() -> couponService.downloadCoupon(couponId, mockMember));

		verify(memberCouponRepository, never()).save(any(MemberCoupon.class));
		verify(couponRepository).findById(couponId);
		verify(memberCouponRepository).existsByCouponId(couponId);
		verify(mockCoupon, never()).reduceStock();

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ALREADY_HAS_COUPON);
	}



	private CreateCouponRequest createCouponRequest() {
		return new CreateCouponRequest(
			"coupon",
			"coupon test",
			CouponType.FIXED,
			1000,
			100,
			1000,
			10,
			null,
			List.of(1L)
		);
	}
}