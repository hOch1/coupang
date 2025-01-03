package ecommerce.coupang.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.dto.response.category.CategoryResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

	private final Long productId;
	private final Long productVariantId;
	private final String name;
	private final int price;
	private final ProductStatus status;
	private final StoreResponse store;
	private final CategoryResponse category;
	private final boolean hasCoupon;
}
