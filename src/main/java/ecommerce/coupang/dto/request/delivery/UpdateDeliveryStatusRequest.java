package ecommerce.coupang.dto.request.delivery;

import ecommerce.coupang.domain.order.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateDeliveryStatusRequest {

	private final DeliveryStatus status;
}
