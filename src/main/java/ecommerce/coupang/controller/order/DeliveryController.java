package ecommerce.coupang.controller.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.dto.request.delivery.AddDeliveryInfoRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.dto.request.delivery.UpdateDeliveryStatusRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.delivery.DeliveryResponse;
import ecommerce.coupang.service.delivery.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Tag(name = "배송 API V1", description = "배송 관련 API")
public class DeliveryController {

	private final DeliveryService deliveryService;

	@PatchMapping("/{deliveryId}")
	@Operation(summary = "배송 정보 등록 API", description = "배송 정보를 등록 API")
	public ResponseEntity<Void> updateDelivery(
		@PathVariable Long deliveryId,
		@RequestBody AddDeliveryInfoRequest request) throws CustomException {

		deliveryService.addDeliveryInfo(request, deliveryId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{deliverId}/status")
	@Operation(summary = "배송 상태 변경 API", description = "배송 상태를 변경합니다.")
	public ResponseEntity<Void> updateDeliveryStatus(
		@PathVariable Long deliverId,
		@RequestBody UpdateDeliveryStatusRequest request) throws CustomException {

		deliveryService.updateDeliveryStatus(deliverId, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{code}")
	@Operation(summary = "배송 조회 API", description = "배송 조회 API")
	public ResponseEntity<Result<DeliveryResponse>> findDelivery(
		@PathVariable String code) throws CustomException {

		Delivery delivery = deliveryService.findDelivery(code);
		return ResponseEntity.ok(new Result<>(DeliveryResponse.from(delivery)));
	}
}
