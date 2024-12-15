package ecommerce.coupang.service.order;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.dto.response.order.OrderResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("OrderService")
public interface OrderService {

	/**
	 * 상품 직접 주문
	 * @param request 주문 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 주문
	 */
	@LogAction("상품 직접 주문")
	Order createOrderByProduct(CreateOrderRequest request, Member member) throws CustomException;

	/**
	 * 장바구니 상품 주문
	 * @param member 요청한 회원
	 * @return 생성한 주문
	 */
	@LogAction("장바구니 상품 주문")
	Order createOrderByCart(Member member);

	/**
	 * 주문 목록 조회
	 * @param member 요청한 회원
	 * @return 주문 목록
	 */
	@LogAction("주문 목록 조회")
	List<Order> findOrders(Member member);

	/**
	 * 주문 상세 조회
	 * @param orderId 주문 ID
	 * @param member 요청한 회원
	 * @return 주문 내역
	 */
	@LogAction("주문 상세 조회")
	Order findOrder(Long orderId, Member member);

	/**
	 * 주문 취소
	 * @param orderId 주문 ID
	 * @param member 요청한 회원
	 * @return 취소한 주문 ID
	 */
	@LogAction("주문 취소")
	Long cancelOrder(Long orderId, Member member);
}
