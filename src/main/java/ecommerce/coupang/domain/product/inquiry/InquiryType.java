package ecommerce.coupang.domain.product.inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InquiryType {

	DELIVERY("배송문의"),
	PRODUCT("싱품문의"),
	PAY("결제문의"),
	ETC("기타")

	;

	private final String displayName;
}
