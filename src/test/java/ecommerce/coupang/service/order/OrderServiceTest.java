package ecommerce.coupang.service.order;

import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.*;
import ecommerce.coupang.dto.request.order.OrderByCartRequest;
import ecommerce.coupang.dto.request.order.OrderByProductRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.service.member.query.AddressQueryService;
import ecommerce.coupang.service.order.strategy.CartOrderStrategy;
import ecommerce.coupang.service.order.strategy.OrderStrategy;
import ecommerce.coupang.service.order.strategy.OrderStrategyProvider;
import ecommerce.coupang.service.order.strategy.ProductOrderStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AddressQueryService addressQueryService;

    @Mock
    private OrderStrategyProvider orderStrategyProvider;

    private Member mockMember;
    private Address mockAddress;
    private Order mockOrder;

    @BeforeEach
    public void beforeEach() {
        mockMember = mock(Member.class);
        mockAddress = mock(Address.class);
        mockOrder = mock(Order.class);
    }


    @Test
    @DisplayName("상품 직접 주문 테스트")
    void createOrderByProduct() throws CustomException {
        OrderByProductRequest request = productRequest();
        OrderStrategy<OrderByProductRequest> mockStrategy = mock(ProductOrderStrategy.class);

        when(orderStrategyProvider.getStrategy(any(Class.class))).thenReturn(mockStrategy);
        when(addressQueryService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(mockStrategy.createOrder(request, mockMember, mockAddress)).thenReturn(mockOrder);

        Order order = orderService.createOrder(request, mockMember);

        verify(orderRepository).save(any(Order.class));
        assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("장바구니 상품 주문 테스트")
    void createOrderByCart() throws CustomException {
        OrderByCartRequest request = cartRequest();
        OrderStrategy<OrderByCartRequest> mockStrategy = mock(CartOrderStrategy.class);

        when(orderStrategyProvider.getStrategy(any(Class.class))).thenReturn(mockStrategy);
        when(addressQueryService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(mockStrategy.createOrder(request, mockMember, mockAddress)).thenReturn(mockOrder);

        Order order = orderService.createOrder(request, mockMember);

        verify(orderRepository).save(any(Order.class));
        assertThat(order).isNotNull();
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void cancelOrder() throws CustomException {
        Long orderId = 1L;
        when(orderRepository.findByIdWithMemberAndAddress(orderId)).thenReturn(Optional.of(mockOrder));

        Order order = orderService.cancelOrder(orderId, mockMember);

        verify(orderRepository).findByIdWithMemberAndAddress(orderId);
        verify(order).validateOrderOwner(mockMember);
        verify(order).cancel();

        assertThat(order).isEqualTo(mockOrder);
    }

    @Test
    @DisplayName("주문 취소 테스트 - 실패 (주문 찾을 수 없음)")
    void cancelOrderFailOrderNotFound() {
        Long orderId = 1L;
        when(orderRepository.findByIdWithMemberAndAddress(orderId)).thenReturn(Optional.empty());

        CustomException customException = assertThrows(CustomException.class, () -> orderService.cancelOrder(orderId, mockMember));

        verify(orderRepository).findByIdWithMemberAndAddress(orderId);

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);
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