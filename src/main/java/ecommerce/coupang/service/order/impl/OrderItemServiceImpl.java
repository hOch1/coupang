package ecommerce.coupang.service.order.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.order.OrderItemRepository;
import ecommerce.coupang.service.order.OrderItemService;
import ecommerce.coupang.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

	private final OrderItemRepository orderItemRepository;
	private final ProductDetailService productDetailService;

	@Override
	@Transactional
	public OrderItem save(Order order, CreateOrderRequest request) throws CustomException {
		ProductDetail productDetail = productDetailService.findProductDetail(request.getProductId());
		OrderItem orderItem = OrderItem.create(order, productDetail, request);

		return orderItemRepository.save(orderItem);
	}
}
