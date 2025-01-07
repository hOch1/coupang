package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.dto.request.product.option.CategoryOptionsRequest;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
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
	private final List<CreateProductVariantRequest> variants;
}
