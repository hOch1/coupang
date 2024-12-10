package ecommerce.coupang.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

	private final Long memberId;
	private final String token;

	public static LoginResponse from(Long memberId, String token) {
		return new LoginResponse(memberId, token);
	}
}
