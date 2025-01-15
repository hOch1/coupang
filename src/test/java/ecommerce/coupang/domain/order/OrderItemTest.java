package ecommerce.coupang.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.member.MemberCoupon;
import ecommerce.coupang.domain.product.variant.ProductVariant;

class OrderItemTest {

	private final Order order = mock(Order.class);
	private final ProductVariant productVariant = mock(ProductVariant.class);
	private final MemberCoupon memberCoupon = mock(MemberCoupon.class);
	private final Delivery delivery = mock(Delivery.class);

	private OrderItem orderItem;

	@BeforeEach
	void beforeEach() {
		orderItem = new OrderItem(order, productVariant, memberCoupon, 1000, 1, 10, 990);
	}

	@Test
	@DisplayName("주문 상품 취소 테스트")
	void cancel() throws CustomException {
		orderItem.setDelivery(delivery);

		orderItem.cancel();

		verify(delivery).cancel();
		verify(productVariant).addStock(1);
		verify(productVariant).decreaseSalesCount(1);
	}
}