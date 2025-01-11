package ecommerce.coupang.service.order;

import java.util.Map;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
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
import ecommerce.coupang.service.order.strategy.OrderType;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@LogLevel("OrderService")
public class OrderService {

	private final OrderRepository orderRepository;
	private final AddressQueryService addressQueryService;
	private final OrderStrategyProvider orderStrategyProvider;

	/**
	 * 주문 생성
	 * @param request 주문 요청 정보
	 * @param member 요청한 회원
	 * @return 주문
	 * @param <T> CreateOrderRequest 를 상속한 Request
	 */
	@LogAction("주문 생성")
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
	public Order cancelOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findByIdWithMemberAndAddress(orderId)
				.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		order.validateOrderOwner(member);
		order.cancel();

		return order;
	}
}
