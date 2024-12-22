package ecommerce.coupang.dto.response.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final StoreResponse store;
	private final CategoryResponse category;

	public static ProductResponse from(Product product) {
		return null;
	}
}
