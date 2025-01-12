package ecommerce.coupang.service.member;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.member.AddressRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@LogLevel("AddressService")
public class AddressService {

	private final AddressRepository addressRepository;

	/**
	 * 주소 추가
	 * @param request 주소 추가 요청 정보
	 * @param member 요청한 회원
	 * @return 추가한 주소 ID
	 */
	@LogAction("주소 추가")
	public Address addAddress(AddAddressRequest request, Member member) {
		List<Address> addressList = addressRepository.findByMemberId(member.getId());
		Address address;

		if (addressList.isEmpty())	// 처음 추가하는 주소는 강제로 기본 주소로 추가
			address = Address.of(request, member, true);
		else {
			if (request.isDefault())
				unsetDefaultAddress(member.getId());

			address = Address.of(request, member, request.isDefault());
		}

		return addressRepository.save(address);
	}

	/**
	 * 기본 주소 변경 (기존 주소가 있을 경우)
	 * @param addressId 변경할 주소 식별자
	 * @param member 요청한 회원
	 * @return 변경한 주소 ID
	 */
	@LogAction("기본 주소 변경")
	public Address setDefaultAddress(Long addressId, Member member) throws CustomException {
		unsetDefaultAddress(member.getId());

		Address newDefaultAddress = addressRepository.findById(addressId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

		newDefaultAddress.setAsDefault();
		return newDefaultAddress;
	}

	/**
	 * 주소 수정
	 * @param addressId 변경할 주소 식별자
	 * @param request 주소 수정 요청 정보
	 * @return 수정한 주소 ID
	 */
	@LogAction("주소 수정")
	public Address updateAddress(Long addressId, UpdateAddressRequest request) throws CustomException {
		Address address = addressRepository.findById(addressId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

		address.update(request);
		return address;
	}

	private void unsetDefaultAddress(Long memberId) {
		addressRepository.findByMemberIdAndIsDefaultTrue(memberId).ifPresent(Address::unsetAsDefault);
	}
}
