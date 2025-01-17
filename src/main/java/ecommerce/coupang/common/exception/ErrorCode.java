package ecommerce.coupang.common.exception;

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
	REVIEW_NOT_FOUND(404, "리뷰를 찾을 수 없습니다."),
	INQUIRY_NOT_FOUND(404, "해당 문의를 찾을 수 없습니다."),
	ANSWER_NOT_FOUND(404, "답변을 찾을 수 없습니다."),
	COUPON_NOT_FOUND(404, "쿠폰을 찾을 수 없습니다."),

	PASSWORD_NOT_MATCHED(400, "비밀번호가 맞지 앉습니다."),
	ALREADY_EXITS_EMAIL(400, "중복된 이메일 입니다."),
	ALREADY_EXITS_PHONE(400, "중복된 핸드폰 번호 입니다."),
	ALREADY_EXITS_STORE_NUMBER(400, "이미 등록된 사업자 번호 입니다."),
	ALREADY_ROLE_SELLER(400, "이미 판매자 권한 입니다."),
	ALREADY_DEFAULT_PRODUCT(400, "해당 상품은 이미 대표 상품 입니다"),
	IS_NOT_BOTTOM_CATEGORY(400, "최하위 카테고리가 아닙니다."),
	QUANTITY_IS_WRONG(400, "수량은 1보다 작을 수 없습니다."),
	INVALID_STOCK_QUANTITY(400, "재고는 0보다 작을 수 없습니다."),
	ALREADY_WRITE_REVIEW_PRODUCT(400, "이미 리뷰를 등록한 상품 입니다."),
	CAN_NOT_LIKE_MY_REVIEW(400, "자신이 등록한 리뷰엔 좋아요할 수 없습니다."),
	CAN_NOT_WRITE_MY_PRODUCT(400, "자신의 상품엔 리뷰를 작성할 수 없습니다."),
	CAN_NOT_INQUIRY_MY_PRODUCT(400, "자신의 상품엔 문의를 등록할 수 없습니다."),
	ALREADY_ANSWERED(400, "이미 답변이 등록된 문의 입니다."),
	ALREADY_CANCELLED_ORDER(400, "이미 취소된 주문 입니다."),
	ALREADY_HAS_COUPON(400, "이미 등록한 쿠폰입니다."),
	ALREADY_USE_COUPON(400, "이미 사용한 쿠폰입니다."),
	OPTION_NOT_CONTAINS(400, "필요한 옵션이 없습니다."),

	FORBIDDEN(401, "권한이 없습니다."),

	CART_NOT_FOUND(500, "장바구니를 찾을 수 없습니다."),
	NOT_ENOUGH_QUANTITY(500, "재고가 부족합니다."),
	ALREADY_DELIVERY_START(500, "이미 배송이 시작되었습니다."),
	PRODUCT_STATUS_NOT_ACTIVE(500, "상품이 판매가능한 상태가 아닙니다."),

	PLEASE_RETRY(500, "서버 오류로인해 다시 요청해주세요")

	;

	private final int errorStatus;
	private final String message;
}
