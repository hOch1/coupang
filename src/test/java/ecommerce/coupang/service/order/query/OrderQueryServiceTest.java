package ecommerce.coupang.service.order.query;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.category.VariantOption;
import ecommerce.coupang.domain.category.VariantOptionValue;
import ecommerce.coupang.dto.response.order.OrderDetailResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.order.OrderItemRepository;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;

@ExtendWith(MockitoExtension.class)
class OrderQueryServiceTest {

	@InjectMocks
	private OrderQueryService orderQueryService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	@Mock
	private ProductVariantOptionRepository productVariantOptionRepository;

	private Member mockMember;
	private Address mockAddress;
	private Product mockProduct;
	private ProductVariant mockProductVariant;
	private Order mockOrder;

	@BeforeEach
	public void beforeEach() {
		mockMember = mock(Member.class);
		mockAddress = mock(Address.class);
		mockProduct = mock(Product.class);
		mockProductVariant = mock(ProductVariant.class);
		mockOrder = mock(Order.class);
	}

	@Test
	@DisplayName("주문 목록 조회 테스트")
	void findOrders() {
		when(mockMember.getId()).thenReturn(1L);
		when(orderRepository.findByMemberIdWithAddress(mockMember.getId())).thenReturn(List.of(mockOrder));

		List<Order> orders = orderQueryService.findOrders(mockMember, null, null);

		verify(orderRepository).findByMemberIdWithAddress(mockMember.getId());

		assertThat(orders).isNotEmpty();
		assertThat(orders.size()).isEqualTo(1);
		assertThat(orders.get(0)).isEqualTo(mockOrder);
	}

	@Test
	@DisplayName("주문 단건 조회 테스트")
	void findOrder() throws CustomException {
		Long orderId = 1L;
		OrderItem mockOrderItem = mock(OrderItem.class);
		ProductVariantOption mockProductVariantOption = mock(ProductVariantOption.class);
		VariantOptionValue mockVariantOptionValue = mock(VariantOptionValue.class);
		VariantOption mockVariantOption = mock(VariantOption.class);

		when(mockVariantOptionValue.getVariantOption()).thenReturn(mockVariantOption);
		when(mockProductVariantOption.getVariantOptionValue()).thenReturn(mockVariantOptionValue);
		when(mockOrder.getMember()).thenReturn(mockMember);
		when(mockOrder.getAddress()).thenReturn(mockAddress);
		when(mockOrderItem.getProductVariant()).thenReturn(mockProductVariant);
		when(mockProductVariant.getProduct()).thenReturn(mockProduct);

		when(orderRepository.findByIdWithMemberAndAddress(orderId)).thenReturn(Optional.of(mockOrder));
		when(orderItemRepository.findByOrderId(anyLong())).thenReturn(List.of(mockOrderItem));
		when(productVariantOptionRepository.findByProductVariantId(anyLong())).thenReturn(List.of(mockProductVariantOption));

		OrderDetailResponse response = orderQueryService.findOrder(orderId, mockMember);

		verify(orderRepository).findByIdWithMemberAndAddress(orderId);
		verify(orderItemRepository).findByOrderId(anyLong());
		verify(productVariantOptionRepository).findByProductVariantId(anyLong());

		assertThat(response).isNotNull();
	}

	@Test
	@DisplayName("주문 단건 조회 테스트 - 실패 (주문 찾을 수 없음)")
	void findOrderFailOrderNotFound() {
		Long orderId = 1L;
		when(orderRepository.findByIdWithMemberAndAddress(orderId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class, () -> orderQueryService.findOrder(orderId, mockMember));

		verify(orderRepository).findByIdWithMemberAndAddress(orderId);

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);
	}

	@Test
	@DisplayName("주문 단건 조회 테스트 - 실패 (요청한 회원과 주문한 회원이 다름)")
	void findOrderFailMemberNotMatch() {
		Long orderId = 1L;
		Member otherMember = mock(Member.class);
		when(orderRepository.findByIdWithMemberAndAddress(orderId)).thenReturn(Optional.of(mockOrder));
		when(mockOrder.getMember()).thenReturn(mockMember);

		CustomException customException = assertThrows(CustomException.class, () -> orderQueryService.findOrder(orderId, otherMember));

		verify(orderRepository).findByIdWithMemberAndAddress(orderId);

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
	}
}