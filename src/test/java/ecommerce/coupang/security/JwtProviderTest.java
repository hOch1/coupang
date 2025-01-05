package ecommerce.coupang.security;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Base64;

import ecommerce.coupang.common.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

	private JwtProvider jwtProvider;

	private String base64Secret;

	@BeforeEach
	void before() {
		base64Secret = Base64.getEncoder().encodeToString("testtesttesttesttesttest".getBytes());
		jwtProvider = spy(new JwtProvider(base64Secret));
	}

	@Test
	@DisplayName("토큰 생성 테스트")
	void createTokenTest() {
		String email = "user1@example.com";

		String token = jwtProvider.createToken(email);

		assertThat(token).isNotNull();
		assertThat(jwtProvider.getTokenByMemberEmail(token)).isEqualTo(email);
		assertThat(jwtProvider.validateToken(token)).isTrue();
	}

	@Test
	@DisplayName("토큰 검증 테스트")
	void validateTokenTest() {
		String email = "user1@example.com";

		String token = jwtProvider.createToken(email);

		assertThat(jwtProvider.validateToken(token)).isTrue();
	}

	@Test
	@DisplayName("토큰 검증 테스트 - 실패")
	void validateTokenFailTest() {}

	@Test
	@DisplayName("토큰에서 이메일 추출 테스트")
	void getTokenByMemberEmailTest() {
		String email = "user1@example.com";
		String token = jwtProvider.createToken(email);

		String extractedEmail = jwtProvider.getTokenByMemberEmail(token);

		assertThat(extractedEmail).isEqualTo(email);
	}
}