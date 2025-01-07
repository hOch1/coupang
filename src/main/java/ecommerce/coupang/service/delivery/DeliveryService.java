package ecommerce.coupang.service.delivery;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.dto.request.delivery.UpdateDeliveryRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.order.DeliveryRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@LogLevel("DeliveryService")
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;

	@Transactional
	public Delivery updateDelivery(UpdateDeliveryRequest request, Member member, Long deliveryId) throws
		CustomException {

		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

		if (!delivery.getStore().getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		delivery.setCompanyInfo(request);
		return delivery;
	}

	public Delivery findDelivery(Long deliveryId) {
		return null;
	}

	public List<Delivery> findDeliveryByMember(Member member) {
		return List.of();
	}

	public List<Delivery> findDeliveryByStore(Long StoreId) {
		return List.of();
	}
}
