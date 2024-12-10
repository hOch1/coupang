package ecommerce.coupang.controller.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "주문 API V1", description = "주문 관련 API")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	@Operation(summary = "주문 생성 API", description = "주문을 생성합니다")
	public ResponseEntity<Void> createOrder(
		@RequestBody CreateOrderRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		//orderService.createOrder(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
