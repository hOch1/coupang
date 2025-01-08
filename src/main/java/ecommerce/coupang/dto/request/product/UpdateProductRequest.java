package ecommerce.coupang.dto.request.product;

import java.util.List;

import ecommerce.coupang.dto.request.product.option.CategoryOptionsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {

	private final String name;
	private final String description;
	private final List<CategoryOptionsRequest> categoryOptions;
}
