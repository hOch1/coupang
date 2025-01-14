package ecommerce.coupang.service.order;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.service.discount.DiscountService;
import ecommerce.coupang.service.store.CouponService;

@ExtendWith(MockitoExtension.class)
class OrderItemFactoryTest {

	@InjectMocks
	private OrderItemFactory orderItemFactory;

	@Mock
	private DiscountService discountService;

	@Mock
	private CouponService couponService;

	@Test
	void createOrderItem() {
	}
}