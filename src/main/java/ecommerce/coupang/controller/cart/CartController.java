package ecommerce.coupang.controller.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.cart.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "장바구니 API V1", description = "장바구니 관련 API")
public class CartController {

	private final CartService cartService;


	@PostMapping
	public ResponseEntity<Void> addCart(@RequestBody AddCartRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		cartService.addCart(request, userDetails.getMember());

		return ResponseEntity.ok().build();
	}
}
