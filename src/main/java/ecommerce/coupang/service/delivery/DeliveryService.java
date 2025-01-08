package ecommerce.coupang.service.delivery;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.dto.request.delivery.AddDeliveryInfoRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.dto.request.delivery.UpdateDeliveryStatusRequest;
import ecommerce.coupang.repository.order.DeliveryRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@LogLevel("DeliveryService")
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;

	/**
	 * 배송 정보 등록
	 * @param request 배송 정보 등록 요청
	 * @param deliveryId 배송 ID
	 * @return 배송
	 */
	@Transactional
	public Delivery addDeliveryInfo(AddDeliveryInfoRequest request, Long deliveryId) throws CustomException {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

		delivery.setCompanyInfo(request);
		return delivery;
	}

	/**
	 * 배송 상태 변경
	 * @param deliveryId 배송 ID
	 * @param request 배송 상태 변경 요청 정보
	 * @return 배송
	 */
	@Transactional
	public Delivery updateDeliveryStatus(Long deliveryId, UpdateDeliveryStatusRequest request) throws CustomException {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

		delivery.updateStatus(request.getStatus());
		return delivery;
	}

	/**
	 * 운송장번호로 배송 조회
	 * @param code 운송장번호
	 * @return 배송
	 * @throws CustomException
	 */
	public Delivery findDelivery(String code) throws CustomException {
		return deliveryRepository.findByCode(code)
			.orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));
	}

	public List<Delivery> findDeliveryByMember(Member member) {
		return List.of();
	}

	public List<Delivery> findDeliveryByStore(Long StoreId) {
		return List.of();
	}
}
