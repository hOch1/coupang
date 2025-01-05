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

import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.store.StoreDetailResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.service.store.query.StoreQueryService;
import ecommerce.coupang.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "상점 API V1", description = "상점 관련 API")
public class StoreController {

	private final StoreService storeService;
	private final StoreQueryService storeQueryService;

	@PostMapping
	@Operation(summary = "상점 등록 API", description = "상점을 등록합니다")
	public ResponseEntity<Void> createStore(
		@RequestBody @Valid CreateStoreRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.createStore(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{storeId}")
	@Operation(summary = "상점 상세 조회 API", description = "상점 상세 정보를 조회합니다")
	public ResponseEntity<Result<StoreDetailResponse>> getStoreDetail(
		@PathVariable Long storeId) throws CustomException {

		Store store = storeQueryService.findStore(storeId);
		return ResponseEntity.ok(new Result<>(StoreDetailResponse.from(store)));
	}

	@GetMapping("/my")
	@Operation(summary = "내 상점 목록 조회", description = "나의 상점 목록을 조회합니다")
	public ResponseEntity<Result<List<StoreResponse>>> getMyStore(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		List<Store> stores = storeQueryService.findMyStore(userDetails.getMember());
		List<StoreResponse> responses = stores.stream()
			.map(StoreResponse::from)
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
	@Operation(summary = "상점 삭제 API", description = "상점을 Soft하게 삭제합니다")
	public ResponseEntity<Void> deleteStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		storeService.deleteStore(storeId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
