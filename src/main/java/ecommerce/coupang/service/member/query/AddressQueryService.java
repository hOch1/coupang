package ecommerce.coupang.service.member.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.member.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressQueryService {

	private final AddressRepository addressRepository;

	/**
	 * 본인 주소록 목록 조회
	 * @param member 요청한 회원
	 * @return 요청한 회원 주소 리스트
	 */
	@LogAction("내 주소록 조회")
	public List<Address> getMyAddresses(Member member) {
		return addressRepository.findByMemberId(member.getId());

	}

	/**
	 * 주소록 단건 조회
	 * @param addressId 조회할 주소록 식별자
	 * @return 요청한 주소 상세 내역
	 */
	@LogAction("주소 상세 조회")
	public Address getAddress(Long addressId) throws CustomException {
		return addressRepository.findById(addressId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));
	}
}
