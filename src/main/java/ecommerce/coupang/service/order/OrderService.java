package ecommerce.coupang.service.order;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.common.exception.CustomException;

@LogLevel("OrderService")
public interface OrderService {

	/**
	 * 상품 직접 주문
	 * @param request 주문 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 주문
	 */
	@LogAction("상품 직접 주문")
	Order createOrderByProduct(CreateOrderByProductRequest request, Member member) throws CustomException;

	/**
	 * 장바구니 상품 주문
	 * @param member 요청한 회원
	 * @return 생성한 주문
	 */
	@LogAction("장바구니 상품 주문")
	Order createOrderByCart(CreateOrderByCartRequest request, Member member) throws CustomException;

	/**
	 * 주문 취소
	 * @param orderId 주문 ID
	 * @param member 요청한 회원
	 * @return 취소한 주문 ID
	 */
	@LogAction("주문 취소")
	Order cancelOrder(Long orderId, Member member) throws CustomException;
}
