package ecommerce.coupang.service.delivery;

import java.util.List;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.dto.request.delivery.UpdateDeliveryRequest;
import ecommerce.coupang.common.exception.CustomException;

@LogLevel("DeliveryService")
public interface DeliveryService {

	Delivery updateDelivery(UpdateDeliveryRequest request, Member member, Long deliveryId) throws CustomException;

	Delivery findDelivery(Long deliveryId);

	List<Delivery> findDeliveryByMember(Member member);

	List<Delivery> findDeliveryByStore(Long StoreId);
}
