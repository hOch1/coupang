package ecommerce.coupang.service.order.strategy;

import org.springframework.stereotype.Component;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.dto.request.order.OrderByCartRequest;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.service.order.OrderItemFactory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartOrderStrategy implements OrderStrategy<OrderByCartRequest>{

	private final CartItemRepository cartItemRepository;
	private final OrderItemFactory orderItemFactory;

	@Override
	public Order createOrder(OrderByCartRequest request, Member member, Address address) throws CustomException {
		Order order = Order.createByCart(request, member, address);

		for (OrderByCartRequest.CartItemRequest cartItemRequest : request.getCartItems()) {
			CartItem cartItem = cartItemRepository.findByIdWithStore(cartItemRequest.getCartItemId())
				.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

			cartItem.getProductVariant().verifyStatusAndReduceStock(cartItem.getQuantity());
			OrderItem orderItem = orderItemFactory.createOrderItem(order, cartItem.getProductVariant(), member, cartItem.getQuantity(), cartItemRequest.getCouponId());

			order.addOrderItem(orderItem);
			cartItemRepository.delete(cartItem);
		}

		return order;
	}
}
