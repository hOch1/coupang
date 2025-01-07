package ecommerce.coupang.common.utils;

import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.domain.member.MemberGrade;

public class MemberUtils {

	public static MemberGrade getMemberGrade(CustomUserDetails userDetails) {
		return (userDetails == null || userDetails.getMember() == null)
			? MemberGrade.NORMAL
			: userDetails.getMember().getGrade();
	}
}
