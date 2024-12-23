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
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.store.StoreDetailResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "가게 API V1", description = "가게 관련 API")
public class StoreController {

	private final StoreService storeService;

	@PostMapping
	@Operation(summary = "상점 등록 API", description = "상점을 등록합니다")
	public ResponseEntity<Void> createStore(
		@RequestBody CreateStoreRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.createStore(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{storeId}")
	@Operation(summary = "상점 상세 조회 API", description = "상점 상세 정보를 조회합니다")
	public ResponseEntity<Result<StoreDetailResponse>> getStoreDetail(
		@PathVariable Long storeId) throws CustomException {

		Store store = storeService.findStore(storeId);
		StoreDetailResponse response = StoreDetailResponse.from(store);

		return ResponseEntity.ok(new Result<>(response));
	}

	@GetMapping("/my")
	@Operation(summary = "내 상점 모록 조회", description = "나의 상점 목록을 조회합니다")
	public ResponseEntity<Result<List<StoreDetailResponse>>> getMyStore(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		List<Store> stores = storeService.findMyStore(userDetails.getMember());
		List<StoreDetailResponse> responses = stores.stream()
			.map(StoreDetailResponse::from)
			.toList();

		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@PatchMapping("/{storeId}")
	@Operation(summary = "상점 수정 API", description = "상점 정보를 수정합니다")
	public ResponseEntity<Void> updateStore(
		@RequestBody UpdateStoreRequest request,
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.updateStore(storeId, request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/{storeId}")
	@Operation(summary = "상점 삭제 API", description = "상점을 삭제합니다")
	public ResponseEntity<Void> deleteStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.deleteStore(storeId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
