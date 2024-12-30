package ecommerce.coupang.dto.request.member;

import ecommerce.coupang.domain.member.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddAddressRequest {

	@NotBlank(message = "주소를 입력해주세요.")
	private final String address;

	@NotNull(message = "주소 종류를 입력해주세요.")
	private final AddressType type;

	private final boolean isDefault;
}
