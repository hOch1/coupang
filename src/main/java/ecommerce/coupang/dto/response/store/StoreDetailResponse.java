package ecommerce.coupang.dto.response.store;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreDetailResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final String storeNumber;
	private final LocalDateTime createdAt;

	public static StoreDetailResponse from(Store store) {
		return new StoreDetailResponse(
			store.getId(),
			store.getName(),
			store.getDescription(),
			store.getStoreNumber(),
			store.getCreatedAt()
		);
	}
}
