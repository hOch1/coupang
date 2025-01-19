package ecommerce.coupang.service.order.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.dto.request.order.OrderByCartRequest;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.service.order.OrderItemFactory;
import ecommerce.coupang.service.product.ProductVariantService;

@Component
public class CartOrderStrategy extends AbstractOrderStrategy<OrderByCartRequest>{

	private final CartItemRepository cartItemRepository;

	public CartOrderStrategy(ProductVariantService productVariantService, OrderItemFactory orderItemFactory, CartItemRepository cartItemRepository) {
		super(productVariantService, orderItemFactory);
		this.cartItemRepository = cartItemRepository;
	}

	@Override
	protected List<AbstractOrderStrategy.OrderItemData> getOrderItemData(OrderByCartRequest request) throws CustomException {
		List<AbstractOrderStrategy.OrderItemData> orderItemDataList = new ArrayList<>();

		for (OrderByCartRequest.CartItemRequest cartItemRequest : request.getCartItems()) {
			CartItem cartItem = cartItemRepository.findByIdWithStore(cartItemRequest.getCartItemId())
				.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

			OrderItemData orderItemData = OrderItemData.from(
				cartItem.getProductVariant().getId(),
				cartItem.getQuantity(),
				cartItemRequest.getCouponId()
			);

			orderItemDataList.add(orderItemData);
			cartItemRepository.delete(cartItem);
		}

		return orderItemDataList;
	}

	@Override
	protected void doAfter(OrderByCartRequest request) {
		request.getCartItems().forEach(cartItemRequest ->
				cartItemRepository.deleteById(cartItemRequest.getCartItemId())
			);
	}
}
