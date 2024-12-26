package ecommerce.coupang.dto.response.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.order.OrderStatus;
import ecommerce.coupang.domain.order.Payment;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.response.option.OptionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDetailResponse {

	private final Long id;
	private final String address;
	private final Payment payment;
	private final OrderStatus status;
	private final int totalPrice;
	private final String orderMessage;
	private final LocalDateTime createdAt;
	private final List<OrderItemResponse> orderItems;

	public static OrderDetailResponse from(Order order) {
		return new OrderDetailResponse(
			order.getId(),
			order.getAddress().getAddress(),
			order.getPayment(),
			order.getStatus(),
			order.getTotalPrice(),
			order.getOrderMessage(),
			order.getCreatedAt(),
			new ArrayList<>()
		);
	}

	@Getter
	@AllArgsConstructor
	public static class OrderItemResponse {

		private final Long id;
		private final Long productVariantId;
		private final String productName;
		private final int price;
		private final int quantity;
		private final int totalPrice;
		private final List<OptionResponse> variantOptions;
		private final List<OptionResponse> categoryOptions;

		public static OrderItemResponse from(OrderItem orderItem, List<ProductVariantOption> productVariantOptions, List<ProductCategoryOption> productCategoryOptions) {
			return new OrderItemResponse(
				orderItem.getId(),
				orderItem.getProductVariant().getId(),
				orderItem.getProductVariant().getProduct().getName(),
				orderItem.getPrice(),
				orderItem.getQuantity(),
				orderItem.getTotalPrice(),
				productVariantOptions.stream()
					.map(OptionResponse::productVariantFrom)
					.toList(),
				productCategoryOptions.stream()
					.map(OptionResponse::productCategoryFrom)
					.toList()
			);
		}
	}
}
