package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductVariantRequest {

	private final Long id;
	private final Long price;
	private final List<UpdateVariantOption> variantOptions;

	@Getter
	@AllArgsConstructor
	public static class UpdateVariantOption {
		private Long optionId;
		private Long optionValueId;
	}
}
