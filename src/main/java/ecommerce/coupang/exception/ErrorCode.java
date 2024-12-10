package ecommerce.coupang.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
	ADDRESS_NOT_FOUND(404, "주소를 찾을 수 없습니다."),
	PRODUCT_NOT_FOUND(404, "상품을 찾을 수 없습니다."),
	CATEGORY_NOT_FOUND(404, "카테고리를 찾을 수 없습니다."),
	STORE_NOT_FOUND(404, "가게를 찾을 수 없습니다."),
	OPTION_VALUE_NOT_FOUND(404, "옵션을 찾을 수 없습니다."),

	PASSWORD_NOT_MATCHED(400, "비밀번호가 맞지 앉습니다."),
	ALREADY_EXITS_EMAIL(400, "중복된 이메일 입니다."),
	ALREADY_EXITS_PHONE(400, "중복된 핸드폰 번호 입니다."),
	ALREADY_EXITS_STORE_NUMBER(400, "이미 등록된 사업자 번호 입니다."),
	IS_NOT_BOTTOM_CATEGORY(400, "최하위 카테고리가 아닙니다."),

	FORBIDDEN(401, "권한이 없습니다."),

	CART_NOT_FOUND(500, "장바구니를 찾을 수 없습니다."),


	;

	private final int errorStatus;
	private final String message;
}
