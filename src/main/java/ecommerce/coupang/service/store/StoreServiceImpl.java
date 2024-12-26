package ecommerce.coupang.service.store;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.MemberRole;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService{

	private final StoreRepository storeRepository;

	@Override
	@Transactional
	public Store createStore(CreateStoreRequest request, Member member) throws CustomException {
		if (!member.getRole().equals(MemberRole.SELLER))
			throw new CustomException(ErrorCode.FORBIDDEN);

		if (storeRepository.existsByStoreNumber(request.getStoreNumber()))
			throw new CustomException(ErrorCode.ALREADY_EXITS_STORE_NUMBER);

		Store store = Store.createFromRequest(request, member);
		return storeRepository.save(store);
	}

	@Override
	public List<Store> findMyStore(Member member) {
		return storeRepository.findByMemberId(member.getId());
	}

	@Override
	public Store findStore(Long storeId) throws CustomException {
		return storeRepository.findByIdWithMember(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));
	}

	@Override
	@Transactional
	public Store updateStore(Long storeId, UpdateStoreRequest request, Member member) throws CustomException {
		Store store = storeRepository.findByIdWithMember(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!Objects.equals(store.getMember().getId(), member.getId()))
			throw new CustomException(ErrorCode.FORBIDDEN);

		store.update(request);

		return store;
	}

	@Override
	@Transactional
	public Long deleteStore(Long storeId, Member member) throws CustomException {
		Store store = storeRepository.findByIdWithMember(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!Objects.equals(store.getMember().getId(), member.getId()))
			throw new CustomException(ErrorCode.FORBIDDEN);

		storeRepository.delete(store);

		return storeId;
	}
}
