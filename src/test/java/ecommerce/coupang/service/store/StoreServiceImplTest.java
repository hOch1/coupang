package ecommerce.coupang.service.store;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberRole;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.store.StoreRepository;
import ecommerce.coupang.service.store.query.StoreQueryService;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

	@InjectMocks
	private StoreService storeService;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private StoreQueryService storeQueryService;

	private Member mockMember;
	private Store mockStore;

	@BeforeEach
	public void beforeEach() {
		mockMember = mock(Member.class);
		mockStore = mock(Store.class);
	}

	@Test
	@DisplayName("가게 등록 테스트")
	void createStoreTest() throws CustomException {
		CreateStoreRequest request = new CreateStoreRequest("store1", "테스트 가게", "000-00-0000");
		when(mockStore.getId()).thenReturn(1L);
		when(mockMember.getRole()).thenReturn(MemberRole.SELLER);

		when(storeRepository.existsByStoreNumber(anyString())).thenReturn(false);
		when(storeRepository.save(any(Store.class))).thenReturn(mockStore);

		Store saveStore = storeService.createStore(request, mockMember);

		assertThat(saveStore.getId()).isEqualTo(1L);
		verify(storeRepository).save(any(Store.class));
	}

	@Test
	@DisplayName("가게 등록 테스트 - 실패 (사업자 등록번호 중복)")
	void createStoreFail() {
		CreateStoreRequest request = new CreateStoreRequest("store1", "테스트 가게", "000-00-0000");
		when(mockMember.getRole()).thenReturn(MemberRole.SELLER);

		when(storeRepository.existsByStoreNumber(anyString())).thenReturn(true);

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.createStore(request, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ALREADY_EXITS_STORE_NUMBER);
	}

	@Test
	@DisplayName("가게 수정 테스트")
	void updateStoreTest() throws CustomException {

		doNothing().when(mockStore).validateOwner(mockMember);
		UpdateStoreRequest request = new UpdateStoreRequest("updateName", "updateDescription");

		when(storeQueryService.findStore(1L)).thenReturn(mockStore);

		Store store = storeService.updateStore(1L, request, mockMember);

		assertThat(store).isEqualTo(mockStore);
		verify(mockStore).update(request);
	}

	@Test
	@DisplayName("가게 수정 테스트 - 실패 (가게 없음)")
	void updateStoreTestFailNotFound() throws CustomException {
		UpdateStoreRequest request = new UpdateStoreRequest("updateName", "updateDescription");

		when(storeQueryService.findStore(1L)).thenThrow(new CustomException(ErrorCode.STORE_NOT_FOUND));

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.updateStore(1L, request, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
	}

	@Test
	@DisplayName("가게 수정 테스트 - 실패 (권한 없음)")
	void updateStoreTestFailForbidden() throws CustomException {
		UpdateStoreRequest request = new UpdateStoreRequest("updateName", "updateDescription");

		doThrow(new CustomException(ErrorCode.FORBIDDEN)).when(mockStore).validateOwner(mockMember);
		when(storeQueryService.findStore(1L)).thenReturn(mockStore);

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.updateStore(1L, request, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
		verify(mockStore, never()).update(request);
	}

	@Test
	@DisplayName("가게 삭제 테스트")
	void deleteStoreTest() throws CustomException {
		doNothing().when(mockStore).validateOwner(mockMember);
		when(storeQueryService.findStore(1L)).thenReturn(mockStore);

		Store store = storeService.deleteStore(1L, mockMember);

		verify(store).delete();
		assertThat(store).isNotNull();
	}

	@Test
	@DisplayName("가게 삭제 테스트 - 실패 (가게 없음)")
	void deleteStoreFailNotFound() throws CustomException {
		Member mockMember = mock(Member.class);

		when(storeQueryService.findStore(1L)).thenThrow(new CustomException(ErrorCode.STORE_NOT_FOUND));

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.deleteStore(1L, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
	}

	@Test
	@DisplayName("가게 삭제 테스트 - 실패 (권한 없음)")
	void deleteStoreForbidden() throws CustomException {

		doThrow(new CustomException(ErrorCode.FORBIDDEN)).when(mockStore).validateOwner(mockMember);
		when(storeQueryService.findStore(1L)).thenReturn(mockStore);

		CustomException customException = assertThrows(CustomException.class,
			() -> storeService.deleteStore(1L, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
		verify(storeRepository, never()).delete(any(Store.class));
	}
}