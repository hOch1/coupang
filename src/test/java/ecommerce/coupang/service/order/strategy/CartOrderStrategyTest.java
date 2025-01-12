package ecommerce.coupang.service.order.strategy;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.order.Payment;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.OrderByCartRequest;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.service.order.OrderItemFactory;

@ExtendWith(MockitoExtension.class)
class CartOrderStrategyTest {

	@InjectMocks
	private CartOrderStrategy cartOrderStrategy;

	@Mock
	private CartItemRepository cartItemRepository;

	@Mock
	private OrderItemFactory orderItemFactory;

	private Order mockOrder = mock(Order.class);
	private CartItem mockCartItem = mock(CartItem.class);
	private OrderItem mockOrderItem = mock(OrderItem.class);
	private ProductVariant mockProductVariant = mock(ProductVariant.class);
	private Member mockMember = mock(Member.class);
	private Address mockAddress = mock(Address.class);


	@Test
	@DisplayName("장바구니 상품 주문 전략 테스트")
	void createOrder() throws CustomException {
		OrderByCartRequest request = cartRequest();

		when(mockCartItem.getQuantity()).thenReturn(1);
		when(mockCartItem.getProductVariant()).thenReturn(mockProductVariant);
		when(cartItemRepository.findByIdWithStore(1L)).thenReturn(Optional.of(mockCartItem));
		when(orderItemFactory.createOrderItem(mockOrder, mockProductVariant, mockMember, mockCartItem.getQuantity(), null))
			.thenReturn(mockOrderItem);

		try (MockedStatic<Order> mockedStatic = mockStatic(Order.class)) {
			mockedStatic.when(() -> Order.of(request, mockMember, mockAddress))
				.thenReturn(mockOrder);

			Order order = cartOrderStrategy.createOrder(request, mockMember, mockAddress);

			verify(cartItemRepository).findByIdWithStore(1L);
			verify(orderItemFactory).createOrderItem(mockOrder, mockProductVariant, mockMember, mockCartItem.getQuantity(), null);
			verify(mockProductVariant).verifyStatusAndReduceStock(mockCartItem.getQuantity());
			verify(order).addOrderItem(mockOrderItem);

			assertThat(order).isNotNull();
		}
	}

	@Test
	@DisplayName("장바구니 상품 주문 전략 테스트 - 실패 (장바구니 상품 못찾음)")
	void createOrderFailCartItemNotFound() throws CustomException {
		OrderByCartRequest request = cartRequest();

		when(cartItemRepository.findByIdWithStore(1L)).thenReturn(Optional.empty());

		try (MockedStatic<Order> mockedStatic = mockStatic(Order.class)) {
			mockedStatic.when(() -> Order.of(request, mockMember, mockAddress))
				.thenReturn(mockOrder);

			CustomException customException = assertThrows(CustomException.class,
				() -> cartOrderStrategy.createOrder(request, mockMember, mockAddress));

			verify(cartItemRepository).findByIdWithStore(1L);
			verify(orderItemFactory, never()).createOrderItem(mockOrder, mockProductVariant, mockMember, mockCartItem.getQuantity(), null);
			verify(mockProductVariant, never()).verifyStatusAndReduceStock(mockCartItem.getQuantity());

			assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND);
		}
	}

	private OrderByCartRequest cartRequest() {
		OrderByCartRequest.CartItemRequest cartItemRequest = new OrderByCartRequest.CartItemRequest(
			1L,
			null
		);
		return new OrderByCartRequest(
			1L,
			Payment.CARD,
			"OrderMessage",
			List.of(cartItemRequest)
		);
	}

}