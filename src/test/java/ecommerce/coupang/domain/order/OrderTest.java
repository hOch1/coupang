package ecommerce.coupang.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;

class OrderTest {

	private final Member member = mock(Member.class);
	private final Address address = mock(Address.class);
	private final OrderItem orderItem = mock(OrderItem.class);
	private final Delivery delivery = mock(Delivery.class);
	private Order order;

	@BeforeEach
	void beforeEach() {
		order = new Order(member, address, Payment.CARD, OrderStatus.PAID, "message");
	}

	@Test
	@DisplayName("주문 상품 추가")
	void addOrderItem() {
		when(orderItem.getTotalPrice()).thenReturn(1000);

		order.addOrderItem(orderItem);

		assertThat(order.getOrderItems().size()).isEqualTo(1);
		assertThat(order.getTotalPrice()).isEqualTo(1000);
	}

	@Test
	@DisplayName("주문 취소 테스트")
	void cancel() throws CustomException {
		order.addOrderItem(orderItem);

		order.cancel();

		verify(orderItem).cancel();
		assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
	}

	@Test
	@DisplayName("주문 취소 테스트 - 실패 (이미 취소 상태)")
	void cancel_fail_alreadyCancelled() throws CustomException {
		Order order = new Order(member, address, Payment.CARD, OrderStatus.CANCELLED, "message");

		CustomException customException = assertThrows(CustomException.class, order::cancel);

		verify(orderItem, never()).cancel();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ALREADY_CANCELLED_ORDER);
	}

	@Test
	@DisplayName("주문한 회원 검증 성공")
	void validateOrderOwner() {
		assertDoesNotThrow(() -> order.validateOrderOwner(member));
	}

	@Test
	@DisplayName("주문 회원 검증 실패")
	void validateOrderOwner_fail() {
		Member other = mock(Member.class);

		CustomException customException = assertThrows(CustomException.class, () -> order.validateOrderOwner(other));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
	}
}