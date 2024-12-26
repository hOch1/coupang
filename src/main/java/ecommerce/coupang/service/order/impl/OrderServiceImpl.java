package ecommerce.coupang.service.order.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Delivery;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.domain.order.OrderItem;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.dto.response.order.OrderDetailResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.order.OrderItemRepository;
import ecommerce.coupang.repository.order.OrderRepository;
import ecommerce.coupang.repository.product.ProductCategoryOptionRepository;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.member.AddressService;
import ecommerce.coupang.service.order.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductVariantRepository productVariantRepository;
	private final ProductVariantOptionRepository productVariantOptionRepository;
	private final ProductCategoryOptionRepository productCategoryOptionRepository;
	private final CartItemRepository cartItemRepository;
	private final AddressService addressService;

	@Override
	@Transactional
	public Order createOrderByProduct(CreateOrderByProductRequest request, Member member) throws CustomException {
		Address address = addressService.getAddress(request.getAddressId());
		Order order = Order.createByProduct(request, member, address);

		ProductVariant productVariant = productVariantRepository.findByIdWithStore(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		verifyStatusAndReduceStock(productVariant, request.getQuantity());

		OrderItem orderItem = OrderItem.createByProduct(order, productVariant, request);
		Delivery delivery = Delivery.create(orderItem, productVariant.getProduct().getStore());
		orderItem.setDelivery(delivery);

		order.addOrderItem(orderItem);

		orderRepository.save(order);
		return order;
	}

	@Override
	@Transactional
	public Order createOrderByCart(CreateOrderByCartRequest request, Member member) throws CustomException {
		Address address = addressService.getAddress(request.getAddressId());
		Order order = Order.createByCart(request, member, address);

		for (Long cartItemId : request.getCartItemIds()) {
			CartItem cartItem = cartItemRepository.findByIdWithStore(cartItemId)
				.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

			verifyStatusAndReduceStock(cartItem.getProductVariant(), cartItem.getQuantity());

			OrderItem orderItem = OrderItem.createByCartItem(order, cartItem);
			Delivery delivery = Delivery.create(orderItem, cartItem.getProductVariant().getProduct().getStore());
			orderItem.setDelivery(delivery);

			order.addOrderItem(orderItem);
		}

		orderRepository.save(order);
		return order;
	}

	@Override
	public List<Order> findOrders(Member member) {
		return orderRepository.findByMemberIdWithAddress(member.getId());
	}

	@Override
	public OrderDetailResponse findOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findByIdWithMemberAndAddress(orderId).orElseThrow(() ->
			new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

		OrderDetailResponse response = OrderDetailResponse.from(order);

		for (OrderItem orderItem : orderItems) {
			List<ProductVariantOption> productVariantOptions = productVariantOptionRepository.findByProductVariantId(
				orderItem.getProductVariant().getId());

			List<ProductCategoryOption> productCategoryOptions = productCategoryOptionRepository.findByProductId(
				orderItem.getProductVariant().getProduct().getId());

			OrderDetailResponse.OrderItemResponse orderItemResponse = OrderDetailResponse.OrderItemResponse.from(orderItem,
				productVariantOptions, productCategoryOptions);

			response.getOrderItems().add(orderItemResponse);
		}

		return response;
	}

	@Override
	@Transactional
	public Order cancelOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findById(orderId).orElseThrow(() ->
			new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		order.cancel();
		return order;
	}

	private void verifyStatusAndReduceStock(ProductVariant productVariant, int quantity) throws CustomException {
		if (!productVariant.getStatus().equals(ProductStatus.ACTIVE))
			throw new CustomException(ErrorCode.PRODUCT_STATUS_NOT_ACTIVE);

		productVariant.reduceStock(quantity);
	}
}
