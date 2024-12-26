package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {

	private final Long id;
	private final String name;
	private final String description;
	private final List<UpdateCategoryOptionsRequest> categoryOptions;

	@Getter
	@AllArgsConstructor
	public static class UpdateCategoryOptionsRequest {
		private final Long optionId;
		private final Long optionValueId;
	}
}
