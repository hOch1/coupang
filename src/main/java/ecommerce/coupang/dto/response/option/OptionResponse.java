package ecommerce.coupang.dto.response.option;

import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionResponse {
	private final Long optionId;
	private final String optionName;
	private final Long optionValueId;
	private final String optionValue;

	public static OptionResponse productVariantFrom(ProductVariantOption productVariantOption) {
		return new OptionResponse(
			productVariantOption.getVariantOptionValue().getVariantOption().getId(),
			productVariantOption.getVariantOptionValue().getVariantOption().getDescription(),
			productVariantOption.getVariantOptionValue().getId(),
			productVariantOption.getVariantOptionValue().getDescription()
		);
	}

	public static OptionResponse productCategoryFrom(ProductCategoryOption productCategoryOption) {
		return new OptionResponse(
			productCategoryOption.getCategoryOptionValue().getCategoryOption().getId(),
			productCategoryOption.getCategoryOptionValue().getCategoryOption().getDescription(),
			productCategoryOption.getCategoryOptionValue().getId(),
			productCategoryOption.getCategoryOptionValue().getDescription()
		);
	}
}
