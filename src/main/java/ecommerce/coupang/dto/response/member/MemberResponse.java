package ecommerce.coupang.dto.response.member;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {

	private final Long id;
	private final String name;
	private final String email;
	private final String phoneNumber;
	private final MemberGrade grade;

	public static MemberResponse from(Member member) {
		return new MemberResponse(
			member.getId(),
			member.getName(),
			member.getEmail(),
			member.getPhoneNumber(),
			member.getGrade()
		);
	}
}
