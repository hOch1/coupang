package ecommerce.coupang.dto.request.product.variant;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductStatusRequest {

	@NotNull(message = "변경할 상품 상태를 선택해주세요.")
	private final ProductStatus status;
}
