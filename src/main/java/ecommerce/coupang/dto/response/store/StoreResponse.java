package ecommerce.coupang.dto.response.store;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.dto.response.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final MemberResponse member;

	public static StoreResponse from(Store store) {
		return new StoreResponse(
			store.getId(),
			store.getName(),
			store.getDescription(),
			MemberResponse.from(store.getMember())
		);
	}

	public static StoreResponse fromProductResponse(Store store) {
		return new StoreResponse(
			store.getId(),
			store.getName(),
			store.getDescription(),
			null
		);
	}
}
