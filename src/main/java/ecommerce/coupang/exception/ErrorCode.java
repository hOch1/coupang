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
	CART_ITEM_NOT_FOUND(404, "장바구니에 상품을 찾을 수 없습니다,"),
	ORDER_NOT_FOUND(404, "주문을 찾을 수 없습니다."),
	DELIVERY_NOT_FOUND(404, "배송 정보를 찾을 수 없습니다."),

	PASSWORD_NOT_MATCHED(400, "비밀번호가 맞지 앉습니다."),
	ALREADY_EXITS_EMAIL(400, "중복된 이메일 입니다."),
	ALREADY_EXITS_PHONE(400, "중복된 핸드폰 번호 입니다."),
	ALREADY_EXITS_STORE_NUMBER(400, "이미 등록된 사업자 번호 입니다."),
	ALREADY_ROLE_SELLER(400, "이미 판매자 권한 입니다."),
	IS_NOT_BOTTOM_CATEGORY(400, "최하위 카테고리가 아닙니다."),
	QUANTITY_IS_WRONG(400, "수량은 1보다 작을 수 없습니다."),

	FORBIDDEN(401, "권한이 없습니다."),

	CART_NOT_FOUND(500, "장바구니를 찾을 수 없습니다."),
	NOT_ENOUGH_QUANTITY(500, "재고가 부족합니다."),
	ALREADY_DELIVERY_START(500, "이미 배송이 시작되었습니다."),
	PRODUCT_STATUS_NOT_ACTIVE(500, "상품이 판매가능한 상태가 아닙니다.")


	;

	private final int errorStatus;
	private final String message;
}
