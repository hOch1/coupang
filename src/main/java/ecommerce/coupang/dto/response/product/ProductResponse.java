package ecommerce.coupang.dto.response.product;

import ecommerce.coupang.domain.product.CategoryType;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.response.member.MemberResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

	private final Long id;
	private final String name;
	private final StoreResponse store;
	private final String category;

	public static ProductResponse from(Product product) {

		return new ProductResponse(
			product.getId(),
			product.getName(),
			StoreResponse.from(product.getStore()),
			product.getCategory().getType()
		);
	}
}
