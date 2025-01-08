package ecommerce.coupang.dto.request.product.variant;

import java.util.List;

import ecommerce.coupang.dto.request.product.option.OptionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductVariantRequest {

	private final Long price;
	private final List<OptionRequest> variantOptions;
}
