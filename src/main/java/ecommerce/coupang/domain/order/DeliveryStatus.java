package ecommerce.coupang.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {
	PENDING("배송 대기중"),
	PROGRESS("배송 진행중"),
	COMPLETE("배송 완료");

	private final String name;
}
