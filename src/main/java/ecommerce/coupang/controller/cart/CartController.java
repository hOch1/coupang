package ecommerce.coupang.controller.cart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.dto.response.cart.CartResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.cart.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "장바구니 API V1", description = "장바구니 관련 API")
public class CartController {

	private final CartService cartService;

	@PostMapping("/item")
	@Operation(summary = "장바구니 상품 추가 API", description = "장바구니에 상품을 추가합니다.")
	public ResponseEntity<Void> addCartItem(
		@RequestBody AddCartRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		cartService.addCart(request, userDetails.getMember());

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	@Operation(summary = "장바구니 조회", description = "내 장바구리 목록을 조회합니다.")
	public ResponseEntity<CartResponse> findMyCart(
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		return ResponseEntity.ok(cartService.findMyCart(userDetails.getMember()));
	}

	@PatchMapping("/item/{cartItemId}")
	@Operation(summary = "장바구니 상품 수량 변경", description = "장바구니 상품 수량을 변경합니다")
	public ResponseEntity<Void> updateItemQuantity(
		@PathVariable Long cartItemId,
		@RequestParam(defaultValue = "1") int quantity,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		cartService.updateItemQuantity(cartItemId, quantity, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/item/{cartItemId}/add")
	@Operation(summary = "장바구니 상품 수량 변경 (+1)", description = "장바구니 상품 수량을 하나 더합니다")
	public ResponseEntity<Void> updateItemQuantityAdd(
		@PathVariable Long cartItemId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		cartService.updateItemQuantity(cartItemId, userDetails.getMember(), true);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/item/{cartItemId}/sub")
	@Operation(summary = "장바구니 상품 수량 변경 (-1)", description = "장바구니 상품 수량을 하나 뺍니다")
	public ResponseEntity<Void> updateItemQuantitySub(
		@PathVariable Long cartItemId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		cartService.updateItemQuantity(cartItemId, userDetails.getMember(), false);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}


	@DeleteMapping("/item/{cartItemId}")
	@Operation(summary = "장바구니 상품 제거", description = "장바구니 상품을 제거합니다.")
	public ResponseEntity<Void> removeCartItem(
		@PathVariable Long cartItemId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		cartService.removeItem(cartItemId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping
	@Operation(summary = "장바구니 전체 제거", description = "장바구니 상품 전체를 제거합니다.")
	public ResponseEntity<Void> clearCart(
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		cartService.clearCart(userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
