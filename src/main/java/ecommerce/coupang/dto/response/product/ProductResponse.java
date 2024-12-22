package ecommerce.coupang.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

	private final Long id;
	private final String name;
	private final int price;
	private final ProductStatus status;
	private final StoreResponse store;
	private final CategoryResponse category;

	public static ProductResponse from(ProductVariant productVariant) {
		return new ProductResponse(
			productVariant.getProduct().getId(),
			productVariant.getProduct().getName(),
			productVariant.getPrice(),
			productVariant.getStatus(),
			StoreResponse.from(productVariant.getProduct().getStore()),
			CategoryResponse.includeParentFrom(productVariant.getProduct().getCategory())
		);
	}
}
