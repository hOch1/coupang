package ecommerce.coupang.service.store;

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

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.dto.response.store.StoreResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private StoreServiceImpl storeService;

	@Test
	@DisplayName("가게 등록 테스트")
	void createStoreTest() throws CustomException {
		CreateStoreRequest request = new CreateStoreRequest("store1", "테스트 가게", "000-00-0000");
		Member mockMember = mock(Member.class);
		Store mockStore = mock(Store.class);
		when(mockStore.getId()).thenReturn(1L);

		when(storeRepository.existsByStoreNumber(anyString())).thenReturn(false);
		when(storeRepository.save(any(Store.class))).thenReturn(mockStore);

		Long saveStore = storeService.createStore(request, mockMember);

		assertThat(saveStore).isEqualTo(1L);
		verify(storeRepository).save(any(Store.class));
	}

	@Test
	@DisplayName("가게 등록 테스트 - 실패 (사업자 등록번호 중복)")
	void createStoreFail() {
		CreateStoreRequest request = new CreateStoreRequest("store1", "테스트 가게", "000-00-0000");
		Member mockMember = mock(Member.class);

		when(storeRepository.existsByStoreNumber(anyString())).thenReturn(true);

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.createStore(request, mockMember));

		assertThat(customException.getError()).isEqualTo(ErrorCode.ALREADY_EXITS_STORE_NUMBER.name());
	}

	@Test
	@DisplayName("가게 상세 조회 테스트")
	void getStoreTest() throws CustomException {
		Store mockStore = mock(Store.class);
		Member mockMember = mock(Member.class);

		when(mockStore.getMember()).thenReturn(mockMember);
		when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));

		StoreResponse response = storeService.findStore(1L);

		assertThat(response).isNotNull();
		verify(storeRepository).findById(1L);
	}

	@Test
	@DisplayName("가게 상세 조회 테스트 - 실패 (가게 없음)")
	void getStoreFail() {
		when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.findStore(1L));

		assertThat(customException.getError()).isEqualTo(ErrorCode.STORE_NOT_FOUND.name());
		verify(storeRepository).findById(1L);
	}

	@Test
	@DisplayName("내 가게 목록 조회 테스트")
	void getMyStoreTest() {
		Store mockStore = mock(Store.class);
		Member mockMember = mock(Member.class);
		when(mockMember.getId()).thenReturn(1L);
		when(mockStore.getMember()).thenReturn(mockMember);

		when(storeRepository.findByMemberId(1L)).thenReturn(List.of(mockStore));

		List<StoreResponse> responses = storeService.findMyStore(mockMember);

		assertThat(responses).isNotEmpty();
		verify(storeRepository).findByMemberId(1L);
	}

	@Test
	@DisplayName("가게 수정 테스트")
	void updateStoreTest() throws CustomException {
		Store originalStore = mock(Store.class);
		Member mockMember = mock(Member.class);
		when(mockMember.getId()).thenReturn(1L);
		when(originalStore.getMember()).thenReturn(mockMember);
		UpdateStoreRequest request = new UpdateStoreRequest("updateName", "updateDescription");

		when(storeRepository.findById(1L)).thenReturn(Optional.of(originalStore));

		Long updateStore = storeService.updateStore(1L, request, mockMember);

		assertThat(updateStore).isEqualTo(1L);
		verify(originalStore).update(request);
	}

	@Test
	@DisplayName("가게 수정 테스트 - 실패 (가게 없음)")
	void updateStoreTestFailNotFound() {
		Member mockMember = mock(Member.class);
		UpdateStoreRequest request = new UpdateStoreRequest("updateName", "updateDescription");

		when(storeRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.updateStore(1L, request, mockMember));

		assertThat(customException.getError()).isEqualTo(ErrorCode.STORE_NOT_FOUND.name());
	}

	@Test
	@DisplayName("가게 수정 테스트 - 실패 (권한 없음)")
	void updateStoreTestFailForbidden() {
		Store originalStore = mock(Store.class);
		Member mockMember = mock(Member.class);
		Member otherMember = mock(Member.class);
		when(mockMember.getId()).thenReturn(1L);
		when(otherMember.getId()).thenReturn(2L);
		when(originalStore.getMember()).thenReturn(mockMember);
		UpdateStoreRequest request = new UpdateStoreRequest("updateName", "updateDescription");

		when(storeRepository.findById(1L)).thenReturn(Optional.of(originalStore));

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.updateStore(1L, request, otherMember));

		assertThat(customException.getError()).isEqualTo(ErrorCode.FORBIDDEN.name());
		verify(originalStore, never()).update(request);
	}

	@Test
	@DisplayName("가게 삭제 테스트")
	void deleteStoreTest() throws CustomException {
		Store mockStore = mock(Store.class);
		Member mockMember = mock(Member.class);
		when(mockMember.getId()).thenReturn(1L);
		when(mockStore.getMember()).thenReturn(mockMember);

		when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));

		Long deleteStore = storeService.deleteStore(1L, mockMember);

		assertThat(deleteStore).isEqualTo(1L);
		verify(storeRepository).delete(any(Store.class));
	}

	@Test
	@DisplayName("가게 삭제 테스트 - 실패 (가게 없음)")
	void deleteStoreFailNotFound() {
		Member mockMember = mock(Member.class);

		when(storeRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.deleteStore(1L, mockMember));

		assertThat(customException.getError()).isEqualTo(ErrorCode.STORE_NOT_FOUND.name());
	}

	@Test
	@DisplayName("가게 삭제 테스트 - 실패 (권한 없음)")
	void deleteStoreForbidden() {
		Store mockStore = mock(Store.class);
		Member mockMember = mock(Member.class);
		Member otherMember = mock(Member.class);
		when(mockMember.getId()).thenReturn(1L);
		when(otherMember.getId()).thenReturn(2L);
		when(mockStore.getMember()).thenReturn(mockMember);

		when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.deleteStore(1L, otherMember));

		assertThat(customException.getError()).isEqualTo(ErrorCode.FORBIDDEN.name());
		verify(storeRepository, never()).delete(any(Store.class));
	}
}