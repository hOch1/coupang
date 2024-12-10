package ecommerce.coupang.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest{

	private final String name;
	private final String phoneNumber;
	private final String email;
	private final String password;
}