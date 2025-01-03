package ecommerce.coupang.service.member;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
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
	 * 주소 수정
	 * @param addressId 변경할 주소 식별자
	 * @param request 주소 수정 요청 정보
	 * @return 수정한 주소 ID
	 */
	@LogAction("주소 수정")
	Address updateAddress(Long addressId, UpdateAddressRequest request) throws CustomException;
}
