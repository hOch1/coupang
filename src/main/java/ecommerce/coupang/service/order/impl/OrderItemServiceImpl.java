package ecommerce.coupang.service.order.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
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
	public OrderItem save(Order order, CreateOrderRequest request) throws CustomException {
		// OrderItem orderItem = OrderItem.create(order, , request);
		//
		// return orderItemRepository.save(orderItem);

		return null;
	}
}
