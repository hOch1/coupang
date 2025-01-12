package ecommerce.coupang.service.order.strategy;

import org.springframework.stereotype.Component;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.OrderByProductRequest;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.order.OrderItemFactory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductOrderStrategy implements OrderStrategy<OrderByProductRequest> {

	private final ProductVariantRepository productVariantRepository;
	private final OrderItemFactory orderItemFactory;

	@Override
	public Order createOrder(OrderByProductRequest request, Member member, Address address) throws CustomException {
		Order order = Order.of(request, member, address);
		ProductVariant productVariant = productVariantRepository.findByIdWithStore(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		productVariant.verifyStatusAndReduceStock(request.getQuantity());
		OrderItem orderItem = orderItemFactory.createOrderItem(order, productVariant, member, request.getQuantity(), request.getCouponId());

		order.addOrderItem(orderItem);

		return order;
	}
}
