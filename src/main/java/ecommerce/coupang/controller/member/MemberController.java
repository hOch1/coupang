package ecommerce.coupang.controller.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.response.GlobalResponse;
import ecommerce.coupang.dto.response.member.MemberResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	@Operation(summary = "내정보 조회 API", description = "내 정보를 조회합니다.")
	public ResponseEntity<GlobalResponse<MemberResponse>> getMyInfo(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		return ResponseEntity.ok(
			new GlobalResponse<>(MemberResponse.from(userDetails.getMember()))
		);
	}

	// TODO - 현재 테스트용으로 바로 판매자로 전환, 추후 관리자 수락하에 변환으로 수정
	@PostMapping("/seller")
	@Operation(summary = "판매자 전환 API", description = "회원 권한을 SELLER로 전환")
	public ResponseEntity<Void> changeRoleSeller(
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		userDetails.getMember().changeRoleSeller();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
