package ecommerce.coupang.dto.request.delivery;

import ecommerce.coupang.domain.order.DeliveryCompany;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddDeliveryInfoRequest {

	private final DeliveryCompany deliveryCompany;
	private final String code;
}
