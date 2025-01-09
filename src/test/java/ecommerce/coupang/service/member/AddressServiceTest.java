package ecommerce.coupang.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.AddressType;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.member.AddressRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

	@InjectMocks
	private AddressService addressService;

	@Mock
	private AddressRepository addressRepository;

	private Member mockMember;
	private Address mockAddress;

	@BeforeEach
	public void beforeEach() {
		mockMember = mock(Member.class);
		mockAddress = mock(Address.class);
	}

	@Test
	@DisplayName("주소 추가 테스트 - 기존 주소 없을 시")
	void addAddressTest() {
		AddAddressRequest request = mock(AddAddressRequest.class);

		when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);
		Address address = addressService.addAddress(request, mockMember);

		verify(addressRepository).save(any(Address.class));

		assertThat(address).isNotNull();
	}

	@Test
	@DisplayName("주소 추가 테스트 - 기존 주소 있을시 (isDefault = true)")
	void addAddressExistingAddressDefaultTrueTest() {
		Address defaultAddress = mock(Address.class);

		AddAddressRequest request = mock(AddAddressRequest.class);
		when(request.isDefault()).thenReturn(true);

		when(addressRepository.findByMemberId(anyLong())).thenReturn(List.of(defaultAddress));
		when(addressRepository.findByMemberIdAndIsDefaultTrue(anyLong())).thenReturn(Optional.of(defaultAddress));
		when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);

		Address address = addressService.addAddress(request, mockMember);

		verify(addressRepository).save(any(Address.class));

		assertThat(address).isNotNull();
	}

	@Test
	@DisplayName("주소 추가 테스트 - 기존 주소 있을시 (isDefault = false)")
	void addAddressExistingAddressDefaultFalseTest() {
		Address defaultAddress = mock(Address.class);

		AddAddressRequest request = mock(AddAddressRequest.class);
		when(request.isDefault()).thenReturn(false);

		when(addressRepository.findByMemberId(anyLong())).thenReturn(List.of(defaultAddress));
		when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);

		Address address = addressService.addAddress(request, mockMember);

		verify(addressRepository).save(any(Address.class));

		assertThat(address).isNotNull();
	}

	@Test
	@DisplayName("주소록 수정 테스트")
	void updateAddressTest() throws CustomException {
		UpdateAddressRequest request = new UpdateAddressRequest("서울시 구로구", AddressType.ETC);

		when(addressRepository.findById(anyLong())).thenReturn(Optional.of(mockAddress));

		Address address = addressService.updateAddress(anyLong(), request);

		verify(addressRepository).findById(anyLong());
		verify(mockAddress).update(request);
	}

	@Test
	@DisplayName("주소록 수정 테스트 - 실패 (해당 주소 없음)")
	void updateAddressFail() {
		UpdateAddressRequest request = new UpdateAddressRequest("서울시 구로구", AddressType.ETC);

		when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> addressService.updateAddress(anyLong(), request));

		verify(addressRepository).findById(anyLong());
		verify(mockAddress, never()).update(request);

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);
	}
}