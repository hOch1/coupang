package ecommerce.coupang.service.delivery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.repository.order.DeliveryRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{

	private final DeliveryRepository deliveryRepository;

	@Override
	public Delivery createDelivery(OrderItem orderItem, Store store) {
		return null;
	}
}
