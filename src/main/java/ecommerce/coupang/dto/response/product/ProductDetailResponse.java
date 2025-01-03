package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.dto.response.category.ParentCategoryResponse;
import ecommerce.coupang.dto.response.option.OptionResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import ecommerce.coupang.dto.response.store.coupon.CouponResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailResponse {

	private final Long productId;
	private final Long productVariantId;
	private final String name;
	private final String description;
	private final int price;
	private final int memberDiscountPrice;
	private final int stockQuantity;
	private final ProductStatus status;
	private final ParentCategoryResponse category;
	private final StoreResponse store;
	private final List<OptionResponse> categoryOptions;
	private final List<OptionResponse> variantOptions;
	private final List<CouponResponse> coupons;

	public static ProductDetailResponse from(
		ProductVariant productVariant,
		Integer memberDiscountPrice,
		Category category,
		List<ProductCategoryOption> productCategoryOptions,
		List<ProductVariantOption> productVariantOptions,
		List<CouponProduct> couponProducts) {

		Product product = productVariant.getProduct();
		return new ProductDetailResponse(
			product.getId(),
			productVariant.getId(),
			product.getName(),
			product.getDescription(),
			productVariant.getPrice(),
			memberDiscountPrice,
			productVariant.getStockQuantity(),
			productVariant.getStatus(),
			ParentCategoryResponse.from(category),
			StoreResponse.from(product.getStore()),
			productCategoryOptions.stream()
				.map(OptionResponse::productCategoryFrom)
				.toList(),
			productVariantOptions.stream()
				.map(OptionResponse::productVariantFrom)
				.toList(),
			couponProducts.stream()
				.map(CouponResponse::from)
				.toList()
		);
	}
}
