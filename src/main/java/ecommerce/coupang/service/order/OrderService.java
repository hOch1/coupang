package ecommerce.coupang.service.order;

import java.util.List;
import java.util.Map;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.dto.request.order.OrderByCartRequest;
import ecommerce.coupang.dto.request.order.OrderByProductRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.service.member.query.AddressQueryService;
import ecommerce.coupang.service.order.strategy.CartOrderStrategy;
import ecommerce.coupang.service.order.strategy.OrderStrategy;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@LogLevel("OrderService")
public class OrderService {

	private final OrderRepository orderRepository;
	private final AddressQueryService addressQueryService;
	private final Map<String, OrderStrategy<? extends CreateOrderRequest>> orderStrategyMap;

	/**
	 * 상품 직접 주문
	 * @param request 주문 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 주문
	 */
	@LogAction("상품 직접 주문")
	public Order createOrderByProduct(OrderByProductRequest request, Member member) throws CustomException {
		Address address = addressQueryService.getAddress(request.getAddressId());

		@SuppressWarnings("unchecked")
		OrderStrategy<OrderByProductRequest> orderStrategy =
			(OrderStrategy<OrderByProductRequest>) orderStrategyMap.get("productOrderStrategy");

		Order order = orderStrategy.createOrder(request, member, address);

		orderRepository.save(order);

		return order;
	}

	/**
	 * 장바구니 상품 주문
	 * @param member 요청한 회원
	 * @return 생성한 주문
	 */
	@LogAction("장바구니 상품 주문")
	public Order createOrderByCart(OrderByCartRequest request, Member member) throws CustomException {
		Address address = addressQueryService.getAddress(request.getAddressId());

		@SuppressWarnings("unchecked")
		OrderStrategy<OrderByCartRequest> orderStrategy =
			(OrderStrategy<OrderByCartRequest>) orderStrategyMap.get("cartOrderStrategy");

		Order order = orderStrategy.createOrder(request, member, address);

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
