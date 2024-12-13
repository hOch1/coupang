package ecommerce.coupang.controller.store;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.dto.response.store.StoreResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.store.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "가게 API V1", description = "가게 관련 API")
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<Void> createStore(@RequestBody CreateStoreRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.createStore(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{storeId}")
	public ResponseEntity<StoreResponse> getStoreDetail(@PathVariable Long storeId) throws CustomException {
		Store store = storeService.findStore(storeId);
		return ResponseEntity.ok(StoreResponse.from(store));
	}

	@GetMapping("/my")
	public ResponseEntity<List<StoreResponse>> getMyStore(@AuthenticationPrincipal CustomUserDetails userDetails) {
		List<Store> stores = storeService.findMyStore(userDetails.getMember());
		return ResponseEntity.ok(stores.stream()
			.map(StoreResponse::from)
			.toList());
	}

	@PatchMapping("/{storeId}")
	public ResponseEntity<Void> updateStore(@RequestBody UpdateStoreRequest request,
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.updateStore(storeId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{storeId}")
	public ResponseEntity<Void> deleteStore(@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.deleteStore(storeId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
