package ecommerce.coupang.service.order.impl;

import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.order.*;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.member.AddressService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private AddressService addressService;

    private Member mockMember;
    private Address mockAddress;
    private Product mockProduct;
    private ProductVariant mockProductVariant;
    private Store mockStore;
    private CartItem mockCartItem;
    private Order mockOrder;

    @BeforeEach
    public void beforeEach() {
        mockMember = mock(Member.class);
        mockAddress = mock(Address.class);
        mockProduct = mock(Product.class);
        mockProductVariant = mock(ProductVariant.class);
        mockStore = mock(Store.class);
        mockCartItem = mock(CartItem.class);
        mockOrder = mock(Order.class);
    }


    @Test
    @DisplayName("상품 직접 주문 테스트")
    void createOrderByProduct() throws CustomException {
        CreateOrderByProductRequest request = productRequest();

        when(addressService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(productVariantRepository.findByIdWithStore(request.getProductVariantId())).thenReturn(Optional.of(mockProductVariant));
        when(mockProductVariant.getStatus()).thenReturn(ProductStatus.ACTIVE);
        when(mockProductVariant.getProduct()).thenReturn(mockProduct);
        when(mockProduct.getStore()).thenReturn(mockStore);

        Order order = orderService.createOrderByProduct(request, mockMember);

        verify(productVariantRepository).findByIdWithStore(request.getProductVariantId());
        verify(orderRepository).save(any(Order.class));

        assertThat(order).isNotNull();
        assertThat(order.getAddress()).isEqualTo(mockAddress);
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("상품 직접 주문 테스트 - 실패 (상품 찾을 수 없음)")
    void createOrderByProductFailProductNotFound() throws CustomException {
        CreateOrderByProductRequest request = productRequest();

        when(addressService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(productVariantRepository.findByIdWithStore(request.getProductVariantId())).thenReturn(Optional.empty());

        CustomException customException = assertThrows(CustomException.class, () -> orderService.createOrderByProduct(request, mockMember));

        verify(productVariantRepository).findByIdWithStore(request.getProductVariantId());
        verify(orderRepository, never()).save(any(Order.class));

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("상품 직접 주문 테스트 - 실패 (상품 상태 정상아님)")
    void createOrderByProductFailProductStatusNotActive() throws CustomException {
        CreateOrderByProductRequest request = productRequest();

        when(addressService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(productVariantRepository.findByIdWithStore(request.getProductVariantId())).thenReturn(Optional.of(mockProductVariant));
        when(mockProductVariant.getStatus()).thenReturn(ProductStatus.INACTIVE);

        CustomException customException = assertThrows(CustomException.class, () -> orderService.createOrderByProduct(request, mockMember));

        verify(productVariantRepository).findByIdWithStore(request.getProductVariantId());
        verify(orderRepository, never()).save(any(Order.class));

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_STATUS_NOT_ACTIVE);
    }

    @Test
    @DisplayName("장바구니 상품 주문 테스트")
    void createOrderByCart() throws CustomException {
        CreateOrderByCartRequest request = cartRequest();

        when(addressService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(cartItemRepository.findByIdWithStore(1L)).thenReturn(Optional.of(mockCartItem));
        when(mockCartItem.getProductVariant()).thenReturn(mockProductVariant);
        when(mockProductVariant.getStatus()).thenReturn(ProductStatus.ACTIVE);
        when(mockProductVariant.getProduct()).thenReturn(mockProduct);
        when(mockProduct.getStore()).thenReturn(mockStore);

        Order order = orderService.createOrderByCart(request, mockMember);

        verify(cartItemRepository).findByIdWithStore(1L);
        verify(orderRepository).save(any(Order.class));

        assertThat(order).isNotNull();
        assertThat(order.getAddress()).isEqualTo(mockAddress);
        assertThat(order.getOrderItems().size()).isEqualTo(1);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("장바구니 상품 주문 테스트 - 실패 (장바구니 상품 찾을 수 없음)")
    void createOrderByCartFailCartItemNotFound() throws CustomException {
        CreateOrderByProductRequest request = productRequest();

        when(addressService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(productVariantRepository.findByIdWithStore(request.getProductVariantId())).thenReturn(Optional.empty());

        CustomException customException = assertThrows(CustomException.class, () -> orderService.createOrderByProduct(request, mockMember));

        verify(productVariantRepository).findByIdWithStore(request.getProductVariantId());
        verify(orderRepository, never()).save(any(Order.class));

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("장바구니 상품 주문 테스트 - 실패 (상품 상태 정상아님)")
    void createOrderByCartFailProductStatusNotActive() throws CustomException {
        CreateOrderByCartRequest request = cartRequest();

        when(addressService.getAddress(request.getAddressId())).thenReturn(mockAddress);
        when(cartItemRepository.findByIdWithStore(1L)).thenReturn(Optional.of(mockCartItem));
        when(mockCartItem.getProductVariant()).thenReturn(mockProductVariant);
        when(mockProductVariant.getStatus()).thenReturn(ProductStatus.INACTIVE);

        CustomException customException = assertThrows(CustomException.class, () -> orderService.createOrderByCart(request, mockMember));

        verify(cartItemRepository).findByIdWithStore(1L);
        verify(orderRepository, never()).save(any(Order.class));

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_STATUS_NOT_ACTIVE);
    }

    @Test
    @DisplayName("주문 목록 조회 테스트")
    void findOrders() {
        when(mockMember.getId()).thenReturn(1L);
        when(orderRepository.findByMemberIdWithAddress(mockMember.getId())).thenReturn(List.of(mockOrder));

        List<Order> orders = orderService.findOrders(mockMember);

        verify(orderRepository).findByMemberIdWithAddress(mockMember.getId());

        assertThat(orders).isNotEmpty();
        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0)).isEqualTo(mockOrder);
    }

    @Test
    @DisplayName("주문 단건 조회 테스트")
    void findOrder() throws CustomException {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(mockOrder.getMember()).thenReturn(mockMember);

        Order order = orderService.findOrder(orderId, mockMember);

        verify(orderRepository).findById(orderId);

        assertThat(order).isNotNull();
        assertThat(order).isEqualTo(mockOrder);
    }

    @Test
    @DisplayName("주문 단건 조회 테스트 - 실패 (주문 찾을 수 없음)")
    void findOrderFailOrderNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        CustomException customException = assertThrows(CustomException.class, () -> orderService.findOrder(orderId, mockMember));

        verify(orderRepository).findById(orderId);

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);
    }

    @Test
    @DisplayName("주문 단건 조회 테스트 - 실패 (요청한 회원과 주문한 회원이 다름)")
    void findOrderFailMemberNotMatch() {
        Long orderId = 1L;
        Member otherMember = mock(Member.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(mockOrder.getMember()).thenReturn(mockMember);

        CustomException customException = assertThrows(CustomException.class, () -> orderService.findOrder(orderId, otherMember));

        verify(orderRepository).findById(orderId);

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void cancelOrder() throws CustomException {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(mockOrder.getMember()).thenReturn(mockMember);

        Order order = orderService.cancelOrder(orderId, mockMember);

        verify(orderRepository).findById(orderId);
        verify(order).cancel();

        assertThat(order).isEqualTo(mockOrder);
    }

    @Test
    @DisplayName("주문 취소 테스트 - 실패 (주문 찾을 수 없음)")
    void cancelOrderFailOrderNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        CustomException customException = assertThrows(CustomException.class, () -> orderService.findOrder(orderId, mockMember));

        verify(orderRepository).findById(orderId);

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ORDER_NOT_FOUND);
    }

    @Test
    @DisplayName("주문 취소 테스트 - 실패 (요청한 회원과 주문한 회원이 다름)")
    void cancelOrderFailMemberNotMatch() {
        Long orderId = 1L;
        Member otherMember = mock(Member.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(mockOrder.getMember()).thenReturn(mockMember);

        CustomException customException = assertThrows(CustomException.class, () -> orderService.cancelOrder(orderId, otherMember));

        verify(orderRepository).findById(orderId);

        assertThat(customException).isNotNull();
        assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }

    private CreateOrderByCartRequest cartRequest() {
        return new CreateOrderByCartRequest(
                List.of(1L),
                1L,
                Payment.CARD,
                "OrderMessage"
        );
    }

    private CreateOrderByProductRequest productRequest() {
        return new CreateOrderByProductRequest(
                1L,
                1L,
                1,
                Payment.CARD,
                "OrderMessage"
        );
    }

}