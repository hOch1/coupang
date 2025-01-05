package ecommerce.coupang.service.member.query;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
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
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.member.AddressRepository;

@ExtendWith(MockitoExtension.class)
class AddressQueryServiceTest {

	@InjectMocks
	private AddressQueryService addressQueryService;

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
	@DisplayName("내 주소록 조회 테스트")
	void getMyAddressesTest() {
		when(addressRepository.findByMemberId(anyLong())).thenReturn(List.of(mockAddress));
		List<Address> addresses = addressQueryService.getMyAddresses(mockMember);

		assertThat(addresses).isNotEmpty();
	}

	@Test
	@DisplayName("주소록 단건 조회 테스트")
	void getAddressTest() throws CustomException {
		when(addressRepository.findById(anyLong())).thenReturn(Optional.of(mockAddress));
		Address address = addressQueryService.getAddress(anyLong());

		assertThat(address).isNotNull();
	}

	@Test
	@DisplayName("주소록 단건 조회 테스트 - 실패 (해당 주소 없을 시)")
	void getAddressFail() {
		when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> addressQueryService.getAddress(anyLong()));

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ADDRESS_NOT_FOUND);
	}
}