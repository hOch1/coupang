package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductDetailRequest {

	private final int id;
	private final int price;
	private final int stockQuantity;
	private final ProductStatus status;
	private final List<Long> options;
}
