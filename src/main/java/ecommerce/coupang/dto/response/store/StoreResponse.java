package ecommerce.coupang.dto.response.store;

import ecommerce.coupang.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreResponse {

	private final Long id;
	private final String name;

	public static StoreResponse from(Store store) {
		return new StoreResponse(
			store.getId(),
			store.getName()
		);
	}
}
