package ecommerce.coupang.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.service.delivery.DeliveryService;
import ecommerce.coupang.service.member.AddressService;
import ecommerce.coupang.service.order.OrderItemService;
import ecommerce.coupang.service.order.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemService orderItemService;
	private final AddressService addressService;
	private final DeliveryService deliveryService;

	@Override
	@Transactional
	public Order createOrderByProduct(CreateOrderRequest request, Member member) throws CustomException {
		Address address = addressService.getAddress(request.getAddressId());
		Order order = Order.createByProduct(request, member, address);
		Order saveOrder = orderRepository.save(order);

		OrderItem orderItem = orderItemService.save(saveOrder, request);
		deliveryService.createDelivery(orderItem);
		return saveOrder;
	}

	@Override
	public Order createOrderByCart(Member member) {

		return null;
	}

	@Override
	public List<Order> findOrders(Member member) {
		return orderRepository.findByMember(member);
	}

	@Override
	public Order findOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findById(orderId).orElseThrow(() ->
			new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		return order;
	}

	@Override
	public Order cancelOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findById(orderId).orElseThrow(() ->
			new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		order.cancel();
		return order;
	}
}
