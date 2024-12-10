package ecommerce.coupang.dto.request.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateStoreRequest {

	private final String name;
	private final String description;
	private final String storeNumber;
}
