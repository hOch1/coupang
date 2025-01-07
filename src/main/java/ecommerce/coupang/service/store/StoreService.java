package ecommerce.coupang.service.store;


import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.common.utils.store.StoreUtils;
import ecommerce.coupang.service.store.query.StoreQueryService;
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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@LogLevel("StoreService")
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreQueryService storeQueryService;

	/**
	 * 상점 등록
	 * @param request 상점 생성 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 상점
	 */
	@LogAction("상점 등록")
	public Store createStore(CreateStoreRequest request, Member member) throws CustomException {
		if (!member.getRole().equals(MemberRole.SELLER))
			throw new CustomException(ErrorCode.FORBIDDEN);

		if (storeRepository.existsByStoreNumber(request.getStoreNumber()))
			throw new CustomException(ErrorCode.ALREADY_EXITS_STORE_NUMBER);

		Store store = Store.createFromRequest(request, member);
		return storeRepository.save(store);
	}

	/**
	 * 상점 수정
	 * @param storeId 상점 식별자
	 * @param request 상점 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 상점
	 */
	@LogAction("상점 수정")
	public Store updateStore(Long storeId, UpdateStoreRequest request, Member member) throws CustomException {
		Store store = storeQueryService.findStore(storeId);
		StoreUtils.validateStoreOwner(store, member);

		store.update(request);

		return store;
	}

	/**
	 * 상점 삭제 (Soft)
	 * @param storeId 상점 ID
	 * @param member 요청한 회원
	 * @return 삭제한 가게
	 */
	@LogAction("상점 삭제")
	public Store deleteStore(Long storeId, Member member) throws CustomException {
		Store store = storeQueryService.findStore(storeId);
		StoreUtils.validateStoreOwner(store, member);

		store.delete();

		return store;
	}
}
