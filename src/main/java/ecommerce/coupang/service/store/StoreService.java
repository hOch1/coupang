package ecommerce.coupang.service.store;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.common.exception.CustomException;

@LogLevel("StoreService")
public interface StoreService {

	/**
	 * 상점 등록
	 * @param request 상점 생성 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 상점
	 * @throws CustomException
	 */
	@LogAction("상점 등록")
	Store createStore(CreateStoreRequest request, Member member) throws CustomException;

	/**
	 * 상점 수정
	 * @param storeId 상점 식별자
	 * @param request 상점 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 상점
	 * @throws CustomException
	 */
	@LogAction("상점 수정")
	Store updateStore(Long storeId, UpdateStoreRequest request, Member member) throws CustomException;

	/**
	 * 상점 삭제 (Soft)
	 * @param storeId 상점 ID
	 * @param member 요청한 회원
	 * @return 삭제한 가게
	 * @throws CustomException
	 */
	@LogAction("상점 삭제")
	Store deleteStore(Long storeId, Member member) throws CustomException;

	/**
	 * 회원이 해당 상점의 주인인지 확인후 반환
	 * @param storeId 상점 ID
	 * @param member 회원
	 * @return 상점
	 */
	Store validateStoreMember(Long storeId, Member member) throws CustomException;
}
