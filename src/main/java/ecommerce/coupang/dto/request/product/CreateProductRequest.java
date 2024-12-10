package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProductRequest {

	private final String name;
	private final String description;
	private final int price;
	private final int stockQuantity;
	private final Long categoryId;
	private final ProductStatus status;
	private final Long storeId;
	private final List<Long> options;
}
