package ecommerce.coupang.service.store;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.exception.CustomException;

@LogLevel("StoreService")
public interface StoreService {

	/**
	 * 가게 등록
	 * @param request 가게 생성 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 가게 ID
	 * @throws CustomException
	 */
	@LogAction("가게 등록")
	Store createStore(CreateStoreRequest request, Member member) throws CustomException;

	/**
	 * 내 가게 목록 조회
	 * @param member 요청한 회원
	 * @return 가게 목록 리스트
	 */
	@LogAction("내 가게 목록 조회")
	List<Store> findMyStore(Member member);

	/**
	 * 가게 상세 정보 조회
	 * @param storeId 가게 식별자
	 * @return 가게 정보
	 * @throws CustomException
	 */
	@LogAction("가게 상제 조회")
	Store findStore(Long storeId) throws CustomException;

	/**
	 * 가게 수정
	 * @param storeId 가게 식별자
	 * @param request 가게 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 가게 식별자
	 * @throws CustomException
	 */
	@LogAction("가게 수정")
	Store updateStore(Long storeId, UpdateStoreRequest request, Member member) throws CustomException;

	/**
	 * 가게 삭제
	 * @param storeId 가게 식별자
	 * @param member 요청한 회원
	 * @return 삭제한 가게 식별자
	 * @throws CustomException
	 */
	@LogAction("가게 삭제")
	Long deleteStore(Long storeId, Member member) throws CustomException;

}
