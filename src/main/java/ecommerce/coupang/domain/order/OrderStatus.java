package ecommerce.coupang.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
	PENDING("결제대기"),
	PAID("결제완료"),
	CANCELLED("주문취소");

	private final String displayName;
}
