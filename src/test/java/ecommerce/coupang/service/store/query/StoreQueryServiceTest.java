package ecommerce.coupang.service.store.query;

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

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.store.StoreRepository;

@ExtendWith(MockitoExtension.class)
class StoreQueryServiceTest {

	@InjectMocks
	private StoreQueryService storeQueryService;

	@Mock
	private StoreRepository storeRepository;

	private Member mockMember;
	private Store mockStore;

	@BeforeEach
	public void beforeEach() {
		mockMember = mock(Member.class);
		mockStore = mock(Store.class);
	}

	@Test
	@DisplayName("가게 조회 테스트")
	void getStoreTest() throws CustomException {
		when(storeRepository.findByIdWithMember(1L)).thenReturn(Optional.of(mockStore));

		Store store = storeQueryService.findStore(1L);

		assertThat(store).isNotNull();
		verify(storeRepository).findByIdWithMember(1L);
	}

	@Test
	@DisplayName("가게 조회 테스트 - 실패 (가게 없음)")
	void getStoreFail() {
		when(storeRepository.findByIdWithMember(anyLong())).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> storeQueryService.findStore(1L));

		assertThat(customException.getError()).isEqualTo(ErrorCode.STORE_NOT_FOUND.name());
		verify(storeRepository).findByIdWithMember(1L);
	}

	@Test
	@DisplayName("내 가게 목록 조회 테스트")
	void getMyStoreTest() {
		when(mockMember.getId()).thenReturn(1L);

		when(storeRepository.findByMemberId(1L)).thenReturn(List.of(mockStore));

		List<Store> stores = storeQueryService.findMyStore(mockMember);

		assertThat(stores).isNotEmpty();
		verify(storeRepository).findByMemberId(1L);
	}
}