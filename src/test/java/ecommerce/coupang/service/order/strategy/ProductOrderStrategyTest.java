package ecommerce.coupang.service.order.strategy;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.order.Payment;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.OrderByProductRequest;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.order.OrderItemFactory;

@ExtendWith(MockitoExtension.class)
class ProductOrderStrategyTest {


	@InjectMocks
	private ProductOrderStrategy productOrderStrategy;

	@Mock
	private OrderItemFactory orderItemFactory;

	@Mock
	private ProductVariantRepository productVariantRepository;

	private Order mockOrder = mock(Order.class);
	private ProductVariant mockProductVariant = mock(ProductVariant.class);
	private OrderItem mockOrderItem = mock(OrderItem.class);
	private Member mockMember = mock(Member.class);
	private Address mockAddress = mock(Address.class);

	@Test
	@DisplayName("상품 직접 주문 전략 테스트")
	void createOrder() throws CustomException {
		OrderByProductRequest request = productRequest();

		when(productVariantRepository.findByIdWithStore(request.getProductVariantId()))
			.thenReturn(Optional.of(mockProductVariant));
		when(orderItemFactory.createOrderItem(mockOrder, mockProductVariant, mockMember, request.getQuantity(), request.getCouponId()))
			.thenReturn(mockOrderItem);

		try (MockedStatic<Order> mockedStatic = mockStatic(Order.class)) {
			mockedStatic.when(() -> Order.of(request, mockMember, mockAddress))
				.thenReturn(mockOrder);

			Order order = productOrderStrategy.createOrder(request, mockMember, mockAddress);

			verify(productVariantRepository).findByIdWithStore(request.getProductVariantId());
			verify(orderItemFactory).createOrderItem(mockOrder, mockProductVariant, mockMember, request.getQuantity(), request.getCouponId());
			verify(mockProductVariant).verifyStatusAndReduceStock(request.getQuantity());
			verify(order).addOrderItem(mockOrderItem);

			assertThat(order).isNotNull();
		}
	}

	@Test
	@DisplayName("상품 직접 주문 전략 테스트 - 실패 (상품 못찾음)")
	void createOrderFailProductNotFound() throws CustomException {
		OrderByProductRequest request = productRequest();

		when(productVariantRepository.findByIdWithStore(request.getProductVariantId())).thenReturn(Optional.empty());

		try (MockedStatic<Order> mockedStatic = mockStatic(Order.class)) {
			mockedStatic.when(() -> Order.of(request, mockMember, mockAddress))
				.thenReturn(mockOrder);

			CustomException customException = assertThrows(CustomException.class,
				() -> productOrderStrategy.createOrder(request, mockMember, mockAddress));

			verify(productVariantRepository).findByIdWithStore(request.getProductVariantId());
			verify(orderItemFactory, never()).createOrderItem(mockOrder, mockProductVariant, mockMember, request.getQuantity(), request.getCouponId());
			verify(mockProductVariant, never()).verifyStatusAndReduceStock(request.getQuantity());

			assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
		}
	}

	private OrderByProductRequest productRequest() {
		return new OrderByProductRequest(
			1L,
			Payment.CARD,
			"OrderMessage",
			1L,
			1,
			1L
		);
	}
}