package ecommerce.coupang.service.store.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreQueryService {

	private final StoreRepository storeRepository;

	/**
	 * 상점 상세 정보 조회
	 * @param storeId 상점 ID
	 * @return 상점 정보
	 * @throws CustomException
	 */
	@LogAction("상점 상세 조회")
	public Store findStore(Long storeId) throws CustomException {
		return storeRepository.findByIdWithMember(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));
	}

	/**
	 * 내 상점 목록 조회
	 * @param member 요청한 회원
	 * @return 상점 목록 리스트
	 */
	@LogAction("내 상점 목록 조회")
	public List<Store> findMyStore(Member member) {
		return storeRepository.findByMemberId(member.getId());
	}
}
