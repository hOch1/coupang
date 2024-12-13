package ecommerce.coupang.service.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.auth.LoginRequest;
import ecommerce.coupang.dto.request.auth.SignupRequest;
import ecommerce.coupang.dto.response.auth.LoginResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.member.MemberRepository;
import ecommerce.coupang.security.JwtProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

	@InjectMocks
	private AuthServiceImpl authService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private JwtProvider jwtProvider;

	@Test
	@DisplayName("로그인 테스트 - 성공")
	void loginSuccess() throws CustomException {
		LoginRequest request = new LoginRequest("user@example.com", "userpw");
		Member mockMember = mock(Member.class);
		when(mockMember.getPassword()).thenReturn("encodedPassword");
		when(mockMember.getId()).thenReturn(1L);

		when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));
		when(passwordEncoder.matches("userpw", "encodedPassword")).thenReturn(true);
		when(jwtProvider.createToken("user@example.com")).thenReturn("token");

		LoginResponse response = authService.login(request);

		assertThat(response).isNotNull();
		assertThat(response.getMemberId()).isEqualTo(1L);
		assertThat(response.getToken()).isEqualTo("token");
		verify(memberRepository).findByEmail("user@example.com");
	}

	@Test
	@DisplayName("로그인 테스트 - 실패 (회원 찾지 못함)")
	void loginFailMemberNotFound() {
		LoginRequest request = new LoginRequest("user@example.com", "userpw");

		when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> authService.login(request));
		verify(memberRepository).findByEmail("user@example.com");
	}

	@Test
	@DisplayName("로그인 테스트 - 실패 (비밀번호 맞지않음)")
	void loginFailPasswordNotMatch() {
		LoginRequest request = new LoginRequest("user@example.com", "userpw");
		Member mockMember = mock(Member.class);
		when(mockMember.getPassword()).thenReturn("encodedPassword");

		when(memberRepository.findByEmail("user@example.com")).thenReturn(Optional.of(mockMember));
		when(passwordEncoder.matches("userpw", "encodedPassword")).thenReturn(false);

		assertThrows(CustomException.class, () -> authService.login(request));
		verify(memberRepository).findByEmail("user@example.com");
	}

	@Test
	@DisplayName("회원가입 테스트 - 성공")
	void signupSuccess() throws CustomException {
		SignupRequest request = new SignupRequest("user1","010-0000-0000", "user1@example.com", "password");
		String encodedPassword = "encodedPassword";
		Member mockMember = mock(Member.class);

		when(mockMember.getId()).thenReturn(1L);
		when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
		when(memberRepository.save(any(Member.class))).thenReturn(mockMember);

		Member saveMember = authService.signup(request);

		assertThat(saveMember.getId()).isEqualTo(mockMember.getId());
		verify(memberRepository).save(any(Member.class));
	}

	@Test
	@DisplayName("회원가입 테스트 - 실패 (이미 가입된 이메일)")
	void signupFailExitsEmail() {
		SignupRequest request = new SignupRequest("user1","010-0000-0000", "user1@example.com", "password");

		when(memberRepository.existsByEmail(anyString())).thenReturn(true);

		assertThrows(CustomException.class, () -> authService.signup(request));
	}

	@Test
	@DisplayName("회원가입 테스트 - 실패 (이미 가입된 핸드폰 번호)")
	void signupFailExitsPhoneNumber() {
		SignupRequest request = new SignupRequest("user1","010-0000-0000", "user1@example.com", "password");

		when(memberRepository.existsByPhoneNumber(anyString())).thenReturn(true);

		assertThrows(CustomException.class, () -> authService.signup(request));
	}
}