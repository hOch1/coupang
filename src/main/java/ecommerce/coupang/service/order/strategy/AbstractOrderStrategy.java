package ecommerce.coupang.service.order.strategy;

import java.util.List;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;
import ecommerce.coupang.service.order.OrderItemFactory;
import ecommerce.coupang.service.product.ProductVariantService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractOrderStrategy<T extends CreateOrderRequest> implements OrderStrategy<T> {

	protected final ProductVariantService productVariantService;
	protected final OrderItemFactory orderItemFactory;

	@Override
	public Order createOrder(T orderRequest, Member member, Address address) throws CustomException {
		Order order = Order.of(orderRequest, member, address);

		List<OrderItemData> orderItemDataList = getOrderItemData(orderRequest);

		for (OrderItemData orderItemData : orderItemDataList) {
			ProductVariant productVariant = productVariantService.reduceStockForOrder(orderItemData.getProductVariantId(), orderItemData.getQuantity());

			OrderItem orderItem = orderItemFactory.createOrderItem(order, productVariant, member,
				orderItemData.getQuantity(), orderItemData.getCouponId());

			order.addOrderItem(orderItem);

			doAfter(orderRequest);
		}

		return order;
	}

	protected abstract List<OrderItemData> getOrderItemData(T request) throws CustomException;
	protected abstract void doAfter(T request);



	@Getter
	@AllArgsConstructor
	protected static class OrderItemData {
		private final Long productVariantId;
		private final int quantity;
		private final Long couponId;

		public static OrderItemData from(Long productVariantId, int quantity, Long couponId) {
			return new OrderItemData(
				productVariantId,
				quantity,
				couponId
			);
		}
	}
}
