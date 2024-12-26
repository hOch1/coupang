package ecommerce.coupang.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductStockRequest {

	private final int stockQuantity;
}
