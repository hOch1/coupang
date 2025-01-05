package ecommerce.coupang.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.auth.LoginRequest;
import ecommerce.coupang.dto.request.auth.SignupRequest;
import ecommerce.coupang.dto.response.auth.LoginResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartRepository;
import ecommerce.coupang.repository.member.MemberRepository;
import ecommerce.coupang.common.security.JwtProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Override
	public LoginResponse login(LoginRequest request) throws CustomException {
		Member member = validateLoginInfo(request);

		String token = jwtProvider.createToken(request.getEmail());

		return LoginResponse.from(member.getId(), token);
	}

	@Override
	@Transactional
	public Member signup(SignupRequest request) throws CustomException {
		validateDuplicateMember(request);

		Member member = Member.createFromSignupRequest(request, passwordEncoder);
		memberRepository.save(member);

		Cart cart = Cart.create(member);
		cartRepository.save(cart);

		return member;
	}

	private Member validateLoginInfo(LoginRequest request) throws CustomException {
		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), member.getPassword()))
			throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);

		return member;
	}

	private void validateDuplicateMember(SignupRequest request) throws CustomException {
		if (memberRepository.existsByEmail(request.getEmail()))
			throw new CustomException(ErrorCode.ALREADY_EXITS_EMAIL);

		if (memberRepository.existsByPhoneNumber(request.getPhoneNumber()))
			throw new CustomException(ErrorCode.ALREADY_EXITS_PHONE);
	}
}