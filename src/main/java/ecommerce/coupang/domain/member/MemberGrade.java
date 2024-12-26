package ecommerce.coupang.domain.member;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberGrade {
    NORMAL("일반"),
    GOLD("골드"),
    VIP("VIP"),
    VVIP("VVIP");


    private final String displayName;
}
