package ecommerce.coupang.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.dto.response.category.CategoryResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

	private final Long productId;
	private final Long productVariantId;
	private final String name;
	private final int price;
	private Integer memberDiscountPrice;
	private final ProductStatus status;
	private final StoreResponse store;
	private final CategoryResponse category;
	private final boolean hasCoupon;

	public ProductResponse(Long productId, Long productVariantId, String name, int price, ProductStatus status,
		StoreResponse store, CategoryResponse category, boolean hasCoupon) {
		this.productId = productId;
		this.productVariantId = productVariantId;
		this.name = name;
		this.price = price;
		this.status = status;
		this.store = store;
		this.category = category;
		this.hasCoupon = hasCoupon;
	}
}
