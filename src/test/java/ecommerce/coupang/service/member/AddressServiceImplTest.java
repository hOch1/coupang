package ecommerce.coupang.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

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
import ecommerce.coupang.dto.response.member.AddressResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.member.AddressRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

	@InjectMocks
	private AddressServiceImpl addressService;

	@Mock
	private AddressRepository addressRepository;

	@Test
	@DisplayName("주소 추가 테스트 - 기존 주소 없을 시")
	void addAddressTest() {
		Member mockMember = mock(Member.class);
		Address mockAddress = mock(Address.class);
		AddAddressRequest request = mock(AddAddressRequest.class);

		when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);
		addressService.addAddress(request, mockMember);

		verify(addressRepository).save(any(Address.class));
	}

	@Test
	@DisplayName("주소 추가 테스트 - 기존 주소 있을시 (isDefault = true)")
	void addAddressExistingAddressDefaultTrueTest() {
		Member mockMember = mock(Member.class);
		Address defaultAddress = mock(Address.class);

		Address mockAddress = mock(Address.class);
		AddAddressRequest request = mock(AddAddressRequest.class);
		when(request.isDefault()).thenReturn(true);

		when(addressRepository.findByMemberId(anyLong())).thenReturn(List.of(defaultAddress));
		when(addressRepository.findByMemberIdAndIsDefaultTrue(anyLong())).thenReturn(Optional.of(defaultAddress));
		when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);
		addressService.addAddress(request, mockMember);

		verify(addressRepository).save(any(Address.class));
	}

	@Test
	@DisplayName("주소 추가 테스트 - 기존 주소 있을시 (isDefault = false)")
	void addAddressExistingAddressDefaultFalseTest() {
		Member mockMember = mock(Member.class);
		Address defaultAddress = mock(Address.class);

		Address mockAddress = mock(Address.class);
		AddAddressRequest request = mock(AddAddressRequest.class);
		when(request.isDefault()).thenReturn(false);

		when(addressRepository.findByMemberId(anyLong())).thenReturn(List.of(defaultAddress));
		when(addressRepository.save(any(Address.class))).thenReturn(mockAddress);
		addressService.addAddress(request, mockMember);

		verify(addressRepository).save(any(Address.class));
	}

	@Test
	@DisplayName("내 주소록 조회 테스트")
	void getMyAddressesTest() {
		Member mockMember = mock(Member.class);
		Address mockAddress = mock(Address.class);

		when(addressRepository.findByMemberId(anyLong())).thenReturn(List.of(mockAddress));
		List<Address> addresses = addressService.getMyAddresses(mockMember);

		assertThat(addresses).isNotEmpty();
	}

	@Test
	@DisplayName("주소록 단건 조회 테스트")
	void getAddressTest() throws CustomException {
		Address mockAddress = mock(Address.class);

		when(addressRepository.findById(anyLong())).thenReturn(Optional.of(mockAddress));
		Address address = addressService.getAddress(anyLong());

		assertThat(address).isNotNull();
	}

	@Test
	@DisplayName("주소록 단건 조회 테스트 - 실패 (해당 주소 없을 시)")
	void getAddressFail() {
		when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(CustomException.class, () -> addressService.getAddress(anyLong()));
	}

	@Test
	@DisplayName("주소록 수정 테스트")
	void updateAddressTest() throws CustomException {
		Address mockAddress = mock(Address.class);
		UpdateAddressRequest request = new UpdateAddressRequest("서울시 구로구", AddressType.ETC);

		when(addressRepository.findById(anyLong())).thenReturn(Optional.of(mockAddress));
		addressService.updateAddress(anyLong(), request);

		verify(mockAddress).update(request);
	}

	@Test
	@DisplayName("주소록 수정 테스트 - 실패")
	void updateAddressFail() {
		UpdateAddressRequest request = new UpdateAddressRequest("서울시 구로구", AddressType.ETC);
		when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(CustomException.class, () -> addressService.updateAddress(anyLong(), request));
	}
}