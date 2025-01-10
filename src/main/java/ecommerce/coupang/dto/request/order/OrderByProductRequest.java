package ecommerce.coupang.dto.request.order;

import ecommerce.coupang.domain.order.Payment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderByProductRequest extends CreateOrderRequest{

	@NotNull(message = "상품을 선택해주세요.")
	private final Long productVariantId;

	@Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
	private final int quantity;

	private final Long couponId;

	public OrderByProductRequest(Long addressId, Payment payment, String orderMessage, Long productVariantId, int quantity, Long couponId) {
		super(addressId, payment, orderMessage);
		this.productVariantId = productVariantId;
		this.quantity = quantity;
		this.couponId = couponId;
	}
}