package ecommerce.coupang.service.store;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.domain.store.statistics.DailySalesStatistics;
import ecommerce.coupang.repository.store.StoreRepository;
import ecommerce.coupang.repository.store.statics.DailySalesStaticsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreStaticsService {

	private final DailySalesStaticsRepository dailySalesStaticsRepository;
	private final StoreRepository storeRepository;

	public DailySalesStatistics findDailySalesStatics(Long storeId, String date, Member member) throws CustomException {
		Store store = storeRepository.findByIdWithMember(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
		store.validateOwner(member);

		LocalDate localDate = LocalDate.parse(date);
		return dailySalesStaticsRepository.findByStoreIdAndDate(storeId, localDate)
			.orElseThrow(() -> new CustomException(ErrorCode.STATICS_NOT_FOUND));
	}
}
