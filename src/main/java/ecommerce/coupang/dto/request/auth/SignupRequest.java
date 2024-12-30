package ecommerce.coupang.dto.request.auth;

import ecommerce.coupang.domain.member.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest{

	@NotBlank(message = "이름을 입력해주세요.")
	private final String name;

	@NotBlank(message = "핸드폰 번호를 입력해주세요.")
	private final String phoneNumber;

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private final String email;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private final String password;

	private final MemberRole role;
}