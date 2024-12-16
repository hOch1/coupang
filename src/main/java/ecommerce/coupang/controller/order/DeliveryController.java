package ecommerce.coupang.controller.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.delivery.CreateDeliveryRequest;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.delivery.DeliveryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PostMapping
	public ResponseEntity<Void> createDelivery(
		@RequestBody CreateDeliveryRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		// deliveryService.createDelivery(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
