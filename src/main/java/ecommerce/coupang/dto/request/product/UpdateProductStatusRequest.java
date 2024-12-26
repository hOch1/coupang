package ecommerce.coupang.dto.request.product;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductStatusRequest {

	private final ProductStatus status;
}
