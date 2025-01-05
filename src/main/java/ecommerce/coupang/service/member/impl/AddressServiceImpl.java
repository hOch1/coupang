package ecommerce.coupang.service.member.impl;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.member.AddressRepository;
import ecommerce.coupang.service.member.AddressService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;

	@Override
	public Address addAddress(AddAddressRequest request, Member member) {
		List<Address> addressList = addressRepository.findByMemberId(member.getId());
		Address address;

		if (addressList.isEmpty())	// 처음 추가하는 주소는 강제로 기본 주소로 추가
			address = Address.createFromRequest(request, member, true);
		else {
			if (request.isDefault())
				unsetDefaultAddress(member.getId());

			address = Address.createFromRequest(request, member, request.isDefault());
		}

		return addressRepository.save(address);
	}

	@Override
	public Address setDefaultAddress(Long addressId, Member member) throws CustomException {
		unsetDefaultAddress(member.getId());

		Address newDefaultAddress = addressRepository.findById(addressId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

		newDefaultAddress.setAsDefault();
		return newDefaultAddress;
	}

	@Override
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
