package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProductRequest {

	private final String name;
	private final String description;
	private final Long categoryId;
	private final Long storeId;
	private final List<CreateDetailRequest> details;

	@Getter
	@AllArgsConstructor
	public static class CreateDetailRequest {

		private final int price;
		private final int stockQuantity;
		private final ProductStatus status;
		private final List<Long> options;
	}
}
