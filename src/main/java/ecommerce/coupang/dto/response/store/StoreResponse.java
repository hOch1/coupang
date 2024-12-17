package ecommerce.coupang.dto.response.store;

import java.time.LocalDateTime;

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
	private final String storeNumber;
	private final LocalDateTime createdAt;
	private final MemberResponse member;

	public static StoreResponse from(Store store, boolean includeMember) {
		return new StoreResponse(
			store.getId(),
			store.getName(),
			store.getDescription(),
			store.getStoreNumber(),
			store.getCreatedAt(),
			includeMember ? MemberResponse.from(store.getMember()) : null
		);
	}
}
