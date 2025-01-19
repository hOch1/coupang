package ecommerce.coupang.service.order.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import ecommerce.coupang.dto.request.order.OrderByProductRequest;
import ecommerce.coupang.service.order.OrderItemFactory;
import ecommerce.coupang.service.product.ProductVariantService;

@Component
public class ProductOrderStrategy extends AbstractOrderStrategy<OrderByProductRequest> {

	public ProductOrderStrategy(ProductVariantService productVariantService, OrderItemFactory orderItemFactory) {
		super(productVariantService, orderItemFactory);
	}

	@Override
	protected List<OrderItemData> getOrderItemData(OrderByProductRequest request) {
		return List.of(OrderItemData.from(
			request.getProductVariantId(),
			request.getQuantity(),
			request.getCouponId()
		));
	}

	@Override
	protected void doAfter(OrderByProductRequest request) {}
}
