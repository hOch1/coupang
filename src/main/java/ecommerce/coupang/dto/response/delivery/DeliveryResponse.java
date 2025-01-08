package ecommerce.coupang.dto.response.delivery;

import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.domain.order.DeliveryCompany;
import ecommerce.coupang.domain.order.DeliveryStatus;
import ecommerce.coupang.dto.response.order.OrderDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryResponse {

	private final Long deliveryId;
	private DeliveryStatus deliveryStatus;
	private DeliveryCompany deliveryCompany;
	private final String code;

	public static DeliveryResponse from(Delivery delivery) {
		return new DeliveryResponse(
			delivery.getId(),
			delivery.getDeliveryStatus(),
			delivery.getDeliveryCompany(),
			delivery.getCode()
		);
	}
}
