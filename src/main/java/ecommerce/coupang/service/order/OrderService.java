package ecommerce.coupang.service.order;

import java.util.List;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.repository.order.OrderItemRepository;
import ecommerce.coupang.service.order.strategy.OrderStrategyProvider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.service.member.query.AddressQueryService;
import ecommerce.coupang.service.order.strategy.OrderStrategy;
import ecommerce.coupang.service.product.ProductVariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@LogLevel("OrderService")
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final AddressQueryService addressQueryService;
	private final OrderStrategyProvider orderStrategyProvider;
	private final ProductVariantService productVariantService;

	/**
	 * 주문 생성
	 * @param request 주문 요청 정보
	 * @param member 요청한 회원
	 * @return 주문
	 * @param <T> CreateOrderRequest 를 상속한 Request
	 */
	@LogAction("주문 생성")
	@Transactional
	public <T extends CreateOrderRequest> Order createOrder(T request, Member member) throws CustomException {
		@SuppressWarnings("unchecked")
		OrderStrategy<T> strategy = (OrderStrategy<T>) orderStrategyProvider.getStrategy(request.getClass());
		Address address = addressQueryService.getAddress(request.getAddressId());

		Order order = strategy.createOrder(request, member, address);

		orderRepository.save(order);

		return order;
	}

	/**
	 * 주문 취소
	 * @param orderId 주문 ID
	 * @param member 요청한 회원
	 * @return 취소한 주문 ID
	 */
	@LogAction("주문 취소")
	@Transactional
	public Order cancelOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findByIdWithMemberAndAddress(orderId)
				.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		order.validateOrderOwner(member);
		order.cancel();

		List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
		for (OrderItem orderItem : orderItems) {
			orderItem.cancel();
			productVariantService.addStockForCancelOrder(orderItem.getProductVariant().getId(), orderItem.getQuantity());
		}

		return order;
	}

	@LogAction("주문 상품 취소")
	@Transactional
	public OrderItem cancelOrderItem(Long orderItemId, Member member) throws CustomException {
		OrderItem orderItem = orderItemRepository.findById(orderItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		orderItem.getOrder().validateOrderOwner(member);
		orderItem.cancel();

		productVariantService.addStockForCancelOrder(orderItem.getProductVariant().getId(), orderItem.getQuantity());

		return orderItem;
	}
}
