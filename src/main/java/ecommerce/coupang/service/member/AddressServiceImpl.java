package ecommerce.coupang.service.member;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
import ecommerce.coupang.dto.response.member.AddressResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;

	@Override
	@Transactional
	@LogAction("주소 추가")
	public Long addAddress(AddAddressRequest request, Member member) {
		List<Address> addressList = addressRepository.findByMemberId(member.getId());
		Address address;

		if (addressList.isEmpty())	// 처음 추가하는 주소는 강제로 기본 주소로 추가
			address = Address.createFromRequest(request, member, true);
		else {
			if (request.isDefault())
				unsetDefaultAddress(member.getId());

			address = Address.createFromRequest(request, member, request.isDefault());
		}

		Address saveAddress = addressRepository.save(address);
		return saveAddress.getId();
	}

	@Override
	@Transactional
	public Long setDefaultAddress(Long addressId, Member member) throws CustomException {
		unsetDefaultAddress(member.getId());

		Address newDefaultAddress = addressRepository.findById(addressId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

		newDefaultAddress.setAsDefault();
		return newDefaultAddress.getId();
	}

	@Override
	public List<AddressResponse> getMyAddresses(Member member) {
		List<Address> addressList = addressRepository.findByMemberId(member.getId());

		return addressList.stream()
			.map(AddressResponse::from)
			.toList();
	}

	@Override
	public AddressResponse getAddress(Long addressId) throws CustomException {
		Address address = addressRepository.findById(addressId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

		return AddressResponse.from(address);
	}

	@Override
	@Transactional
	public Long updateAddress(Long addressId, UpdateAddressRequest request) throws CustomException {
		Address address = addressRepository.findById(addressId)
			.orElseThrow(() -> new CustomException(ErrorCode.ADDRESS_NOT_FOUND));

		address.update(request);
		return addressId;
	}

	private void unsetDefaultAddress(Long memberId) {
		Address defaultAddress = addressRepository.findByMemberIdAndIsDefaultTrue(memberId).orElse(null);

		if (defaultAddress != null) {
			defaultAddress.unsetAsDefault();
		}
	}
}
