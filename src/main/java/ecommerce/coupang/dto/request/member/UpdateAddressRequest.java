package ecommerce.coupang.dto.request.member;

import ecommerce.coupang.domain.member.AddressType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateAddressRequest {

	private final String address;
	private final AddressType type;
}
