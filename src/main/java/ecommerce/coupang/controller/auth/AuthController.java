package ecommerce.coupang.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.auth.LoginRequest;
import ecommerce.coupang.dto.request.auth.SignupRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.auth.LoginResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API V1", description = "회원가입 및 인증 관련 API")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	@Operation(summary = "로그인 API", description = "로그인 진행 후 JWT 발급")
	public ResponseEntity<Result<LoginResponse>> login(
		@RequestBody @Valid LoginRequest request) throws CustomException {

		return ResponseEntity.ok(new Result<>(authService.login(request)));
	}

	@PostMapping("/signup")
	@Operation(summary = "회원가입 API", description = "회원가입 진행")
	public ResponseEntity<Void> signup(
		@RequestBody @Valid SignupRequest signupRequest) throws CustomException {

		authService.signup(signupRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}