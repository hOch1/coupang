package ecommerce.coupang.utils.member;

import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.domain.member.MemberGrade;

public final class MemberUtils {

	public static MemberGrade getMemberGrade(CustomUserDetails userDetails) {
		return (userDetails == null || userDetails.getMember() == null)
			? MemberGrade.NORMAL
			: userDetails.getMember().getGrade();
	}
}
