package ecommerce.coupang.controller.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderStatus;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.dto.request.order.OrderSearchStatus;
import ecommerce.coupang.dto.request.order.OrderSort;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.order.OrderDetailResponse;
import ecommerce.coupang.dto.response.order.OrderResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.order.OrderService;
import ecommerce.coupang.service.order.query.OrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "주문 API V1", description = "주문 관련 API")
public class OrderController {

	private final OrderService orderService;
	private final OrderQueryService orderQueryService;

	@PostMapping("/product")
	@Operation(summary = "상품 주문 API", description = "상품을 직접 주문합니다 ")
	public ResponseEntity<Void> createOrderByProduct(
		@RequestBody @Valid CreateOrderByProductRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		orderService.createOrderByProduct(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/cart")
	@Operation(summary = "장바구니 상품 주문 API", description = "장바구니에 담긴 상품을 주문합니다")
	public ResponseEntity<Void> createOrderByCart(
		@RequestBody @Valid CreateOrderByCartRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		orderService.createOrderByCart(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	@Operation(summary = "주문 목록 조회 API", description = "주문 내역 목록을 조회합니다")
	public ResponseEntity<Result<List<OrderResponse>>> findOrders(
		@RequestParam(required = false) OrderStatus status,
		@RequestParam(required = false, defaultValue = "LATEST") OrderSort sort,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		List<Order> orders = orderQueryService.findOrders(userDetails.getMember(), status, sort);
		List<OrderResponse> responses = orders.stream()
			.map(OrderResponse::from)
			.toList();

		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@GetMapping("/{orderId}")
	@Operation(summary = "주문 상세 조회 API", description = "주문 상세 내역을 조회합니다.")
	public ResponseEntity<Result<OrderDetailResponse>> findOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		OrderDetailResponse response = orderQueryService.findOrder(orderId, userDetails.getMember());
		return ResponseEntity.ok(new Result<>(response));
	}

	@DeleteMapping("/{orderId}")
	@Operation(summary = "주문 취소 API", description = "주문을 취소합니다.")
	public ResponseEntity<Void> cancelOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		orderService.cancelOrder(orderId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}