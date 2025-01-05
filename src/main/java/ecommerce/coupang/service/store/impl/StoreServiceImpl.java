package ecommerce.coupang.service.store.impl;


import ecommerce.coupang.service.store.StoreUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberRole;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.store.StoreRepository;
import ecommerce.coupang.service.store.StoreService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreServiceImpl implements StoreService {

	private final StoreRepository storeRepository;

	@Override
	public Store createStore(CreateStoreRequest request, Member member) throws CustomException {
		if (!member.getRole().equals(MemberRole.SELLER))
			throw new CustomException(ErrorCode.FORBIDDEN);

		if (storeRepository.existsByStoreNumber(request.getStoreNumber()))
			throw new CustomException(ErrorCode.ALREADY_EXITS_STORE_NUMBER);

		Store store = Store.createFromRequest(request, member);
		return storeRepository.save(store);
	}

	@Override
	public Store updateStore(Long storeId, UpdateStoreRequest request, Member member) throws CustomException {
		Store store = findStoreWithMember(storeId);
		StoreUtils.validateStoreOwner(store, member);

		store.update(request);

		return store;
	}

	@Override
	public Store deleteStore(Long storeId, Member member) throws CustomException {
		Store store = findStoreWithMember(storeId);
		StoreUtils.validateStoreOwner(store, member);

		store.delete();

		return store;
	}

	private Store findStoreWithMember(Long storeId) throws CustomException {
		return storeRepository.findByIdWithMember(storeId)
				.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
