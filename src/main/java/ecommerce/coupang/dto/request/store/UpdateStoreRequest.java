package ecommerce.coupang.dto.request.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateStoreRequest {

	private final String name;
	private final String description;
}
