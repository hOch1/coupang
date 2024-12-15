package ecommerce.coupang.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProductRequest {

	private final String name;
	private final String description;

}
