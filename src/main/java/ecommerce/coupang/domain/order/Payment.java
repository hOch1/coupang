package ecommerce.coupang.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Payment {
	CARD("카드결제"),
	TRANSFER("계죄이체")
	;

	private final String displayName;

}
