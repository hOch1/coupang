package ecommerce.coupang.service.order.query;

import static ecommerce.coupang.dto.response.order.OrderDetailResponse.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.order.OrderStatus;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.request.order.OrderSort;
import ecommerce.coupang.dto.response.order.OrderDetailResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.order.OrderItemRepository;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.service.product.option.ProductVariantOptionService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductVariantOptionService productVariantOptionService;

	/**
	 * 주문 목록 조회
	 * @param member 요청한 회원
	 * @return 주문 목록
	 */
	@LogAction("주문 목록 조회")
	public List<Order> findOrders(Member member, OrderStatus status, OrderSort sort) {
		return orderRepository.findOrders(member.getId(), status, sort);
	}

	/**
	 * 주문 상세 조회
	 * @param orderId 주문 ID
	 * @param member 요청한 회원
	 * @return 주문 내역
	 */
	@LogAction("주문 상세 조회")
	public OrderDetailResponse findOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findByIdWithMemberAndAddress(orderId).orElseThrow(() ->
			new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

		OrderDetailResponse response = from(order);

		for (OrderItem orderItem : orderItems) {
			List<ProductVariantOption> productVariantOptions = productVariantOptionService.getProductVariantOptionByProductVariantId(orderItem.getProductVariant().getId());

			OrderItemResponse orderItemResponse = OrderItemResponse.from(orderItem, productVariantOptions);

			response.getOrderItems().add(orderItemResponse);
		}

		return response;
	}
}
