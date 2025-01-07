package ecommerce.coupang.dto.request.product.variant;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductStockRequest {

	@Min(value = 1, message = "상품 수량은 최소 1 이상이어야 합니다.")
	private final int stockQuantity;
}
