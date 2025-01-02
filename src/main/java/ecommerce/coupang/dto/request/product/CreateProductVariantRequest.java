package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProductVariantRequest {
	private final int price;
	private final int stock;
	private final ProductStatus status;
	private final boolean isDefault;
	private final List<VariantOptionRequest> variantOptions;

	@Getter
	@AllArgsConstructor
	public static class VariantOptionRequest {
		private Long optionId;
		private Long optionValueId;
	}

}
