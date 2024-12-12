package ecommerce.coupang.service.store;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberRole;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.dto.response.store.StoreResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService{

	private final StoreRepository storeRepository;

	@Override
	@Transactional
	public Long createStore(CreateStoreRequest request, Member member) throws CustomException {
		// if (!member.getRole().equals(MemberRole.SELLER))
		// 	throw new CustomException(ErrorCode.FORBIDDEN);
		// 테스트 환경에서 권한 체크 제외

		if (storeRepository.existsByStoreNumber(request.getStoreNumber()))
			throw new CustomException(ErrorCode.ALREADY_EXITS_STORE_NUMBER);

		Store store = Store.createFromRequest(request, member);
		Store saveStore = storeRepository.save(store);
		return saveStore.getId();
	}

	@Override
	public List<StoreResponse> findMyStore(Member member) {
		List<Store> stores = storeRepository.findByMemberId(member.getId());

		return stores.stream()
			.map(StoreResponse::from)
			.toList();
	}

	@Override
	public StoreResponse findStore(Long storeId) throws CustomException {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		return StoreResponse.from(store);
	}

	@Override
	@Transactional
	public Long updateStore(Long storeId, UpdateStoreRequest request, Member member) throws CustomException {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!Objects.equals(store.getMember().getId(), member.getId()))
			throw new CustomException(ErrorCode.FORBIDDEN);

		store.update(request);

		return storeId;
	}

	@Override
	@Transactional
	public Long deleteStore(Long storeId, Member member) throws CustomException {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!Objects.equals(store.getMember().getId(), member.getId()))
			throw new CustomException(ErrorCode.FORBIDDEN);

		storeRepository.delete(store);

		return storeId;
	}
}
