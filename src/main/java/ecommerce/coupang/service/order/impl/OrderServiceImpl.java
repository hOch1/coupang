package ecommerce.coupang.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.dto.response.order.OrderResponse;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.service.order.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	@Override
	public Order createOrderByProduct(CreateOrderRequest request, Member member) {
		return null;
	}

	@Override
	public Order createOrderByCart(Member member) {
		return null;
	}

	@Override
	public List<Order> findOrders(Member member) {
		return List.of();
	}

	@Override
	public Order findOrder(Long orderId, Member member) {
		return null;
	}

	@Override
	public Long cancelOrder(Long orderId, Member member) {
		return 0L;
	}
}
