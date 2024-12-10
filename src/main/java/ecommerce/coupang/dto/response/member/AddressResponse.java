package ecommerce.coupang.dto.response.member;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.AddressType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressResponse {

	private final Long id;
	private final String address;
	private final AddressType type;
	private final boolean isDefault;

	public static AddressResponse from(Address address) {
		return new AddressResponse(
			address.getId(),
			address.getAddress(),
			address.getType(),
			address.isDefault()
		);
	}
}
