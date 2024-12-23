package ecommerce.coupang.service.order.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.order.OrderItemRepository;
import ecommerce.coupang.service.order.OrderItemService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

	private final OrderItemRepository orderItemRepository;

	@Override
	@Transactional
	public OrderItem save(Order order, CreateOrderByProductRequest request) throws CustomException {
		// OrderItem orderItem = OrderItem.create(order, , request);
		//
		// return orderItemRepository.save(orderItem);

		return null;
	}
}
