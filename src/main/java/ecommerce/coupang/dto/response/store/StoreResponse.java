package ecommerce.coupang.dto.response.store;

import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.dto.response.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}
