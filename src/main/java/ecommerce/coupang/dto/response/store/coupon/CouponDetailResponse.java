package ecommerce.coupang.dto.response.store.coupon;

import java.time.LocalDateTime;
import java.util.List;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.store.Coupon;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.domain.store.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponDetailResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final int minPrice;
	private final LocalDateTime limitDate;
	private final CouponType type;
	private final Integer discountValue;
	private final Integer limitDiscountPrice;
	private final Integer couponStock;
	private final LocalDateTime createdAt;
	private final List<CouponProductResponse> products;


	public static CouponDetailResponse from(Coupon coupon, List<CouponProduct> couponProducts) {
		return new CouponDetailResponse(
			coupon.getId(),
			coupon.getName(),
			coupon.getDescription(),
			coupon.getMinPrice(),
			coupon.getLimitDate().equals(LocalDateTime.MAX) ? null : coupon.getLimitDate(),
			coupon.getType(),
			coupon.getDiscountValue(),
			coupon.getLimitDiscountPrice() == Integer.MAX_VALUE ? null : coupon.getLimitDiscountPrice(),
			coupon.getCouponStock() == Integer.MAX_VALUE ? null : coupon.getCouponStock(),
			coupon.getCreatedAt(),
			couponProducts.stream()
				.map(CouponProductResponse::from)
				.toList()
		);
	}

	@Getter
	@AllArgsConstructor
	public static class CouponProductResponse {
		private final Long id;
		private final String name;

		public static CouponProductResponse from(CouponProduct couponProduct) {
			Product product = couponProduct.getProduct();
			return new CouponProductResponse(
				product.getId(),
				product.getName()
			);
		}
	}
}
