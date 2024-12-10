package ecommerce.coupang.dto.request.product;

import java.util.Map;

import ecommerce.coupang.domain.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {

	private final String name;
	private final Integer price;
	private final Integer stockQuantity;
	private final ProductStatus status;
}
