package ecommerce.coupang.service.auth;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.auth.LoginRequest;
import ecommerce.coupang.dto.request.auth.SignupRequest;
import ecommerce.coupang.dto.response.auth.LoginResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("AuthService")
public interface AuthService {

	/**
	 * 로그인
	 * @param request 로그인 요청 정보
	 * @return 로그인 응답 정보 (JWT)
	 */
	@LogAction("로그인")
	LoginResponse login(LoginRequest request) throws CustomException;

	/**
	 * 회원가입
	 * @param request 회원가입 요청 정보
	 * @return 회원가입한 회원 ID
	 */
	@LogAction("회원가입")
	Member signup(SignupRequest request) throws CustomException;
}
