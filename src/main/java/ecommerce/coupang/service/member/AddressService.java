package ecommerce.coupang.service.member;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
import ecommerce.coupang.dto.response.member.AddressResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("AddressService")
public interface AddressService {

	/**
	 * 주소 추가
	 * @param request 주소 추가 요청 정보
	 * @param member 요청한 회원
	 * @return 추가한 주소 ID
	 */
	@LogAction("주소 추가")
	Address addAddress(AddAddressRequest request, Member member);

	/**
	 * 기본 주소 변경 (기존 주소가 있을 경우)
	 * @param addressId 변경할 주소 식별자
	 * @param member 요청한 회원
	 * @return 변경한 주소 ID
	 */
	@LogAction("기본 주소 변경")
	Address setDefaultAddress(Long addressId, Member member) throws CustomException;

	/**
	 * 본인 주소록 목록 조회
	 * @param member 요청한 회원
	 * @return 요청한 회원 주소 리스트
	 */
	@LogAction("내 주소록 조회")
	List<Address> getMyAddresses(Member member);

	/**
	 * 주소록 단건 조회
	 * @param addressId 조회할 주소록 식별자
	 * @return 요청한 주소 상세 내역
	 */
	@LogAction("주소 상세 조회")
	Address getAddress(Long addressId) throws CustomException;

	/**
	 * 주소 수정
	 * @param addressId 변경할 주소 식별자
	 * @param request 주소 수정 요청 정보
	 * @return 수정한 주소 ID
	 */
	@LogAction("주소 수정")
	Address updateAddress(Long addressId, UpdateAddressRequest request) throws CustomException;
}
