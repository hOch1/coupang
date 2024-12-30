package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.domain.product.variant.ProductStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateProductRequest {

	@NotEmpty(message = "상품명을 입력해주세요.")
	private final String name;

	@NotEmpty(message = "상품 설명을 입력해주세요.")
	private final String description;

	@NotNull(message = "등록할 카테고리를 선택해주세요.")
	private final Long categoryId;

	@NotEmpty(message = "옵션을 선택해주세요.")
	private final List<CategoryOptionsRequest> categoryOptions;

	@NotEmpty(message = "옵션을 선택해주세요.")
	private final List<VariantRequest> variants;

	@Getter
	@AllArgsConstructor
	public static class CategoryOptionsRequest {
		private final Long optionId;
		private final Long optionValueId;
	}

	@Getter
	@AllArgsConstructor
	public static class VariantRequest {
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
}
