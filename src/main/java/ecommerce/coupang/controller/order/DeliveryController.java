package ecommerce.coupang.controller.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.delivery.UpdateDeliveryRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.delivery.DeliveryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PatchMapping("/{deliveryId}")
	public ResponseEntity<Void> updateDelivery(
		@PathVariable Long deliveryId,
		@RequestBody UpdateDeliveryRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		deliveryService.updateDelivery(request, userDetails.getMember(), deliveryId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
