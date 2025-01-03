package ecommerce.coupang.controller.member;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.member.AddressResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import ecommerce.coupang.service.member.AddressService;
import ecommerce.coupang.service.member.query.AddressQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
@Tag(name = "주소록 API V1", description = "주소록 관련 API")
public class AddressController {

	private final AddressService addressService;
	private final AddressQueryService addressQueryService;

	@PostMapping
	@Operation(summary = "주소 추가 API", description = "주소를 추가합니다")
	public ResponseEntity<Void> addAddress(
		@RequestBody @Valid AddAddressRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		addressService.addAddress(request, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{addressId}/default")
	@Operation(summary = "기본 주소 변경 API", description = "기본 주소를 변경합니다.")
	public ResponseEntity<Void> changeDefaultAddress(
		@PathVariable Long addressId,
		@AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

		addressService.setDefaultAddress(addressId, userDetails.getMember());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping
	@Operation(summary = "내 주소록 조회 API", description = "나의 주소 목록을 조회합니다.")
	public ResponseEntity<Result<List<AddressResponse>>> getMyAddresses(
		@AuthenticationPrincipal CustomUserDetails userDetails) {

		List<Address> addresses = addressQueryService.getMyAddresses(userDetails.getMember());
		List<AddressResponse> responses = addresses.stream()
			.map(AddressResponse::from)
			.toList();

		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}

	@GetMapping("/{addressId}")
	@Operation(summary = "주소 단건 조회 API", description = "단건 주소를 조회합니다.")
	public ResponseEntity<Result<AddressResponse>> getAddress(
		@PathVariable Long addressId) throws CustomException {

		Address address = addressQueryService.getAddress(addressId);
		return ResponseEntity.ok(new Result<>(AddressResponse.from(address)));
	}

	@PatchMapping("/{addressId}")
	@Operation(summary = "주소 수정 API", description = "주소를 수정합니다.")
	public ResponseEntity<Void> updateAddress(
		@PathVariable Long addressId,
		@RequestBody UpdateAddressRequest request) throws CustomException {

		addressService.updateAddress(addressId, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
