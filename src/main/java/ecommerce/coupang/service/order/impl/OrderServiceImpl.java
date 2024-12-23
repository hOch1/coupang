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
import ecommerce.coupang.service.order.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductVariantRepository productVariantRepository;
	private final AddressService addressService;
	private final CartItemRepository cartItemRepository;

	@Override
	@Transactional
	public Order createOrderByProduct(CreateOrderByProductRequest request, Member member) throws CustomException {
		Address address = addressService.getAddress(request.getAddressId());
		Order order = Order.createByProduct(request, member, address);

		ProductVariant productVariant = productVariantRepository.findByIdWithStore(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		verifyStatusAndReduceStock(productVariant, request.getQuantity());

		OrderItem orderItem = OrderItem.createByProduct(order, productVariant, request);
		Delivery.create(orderItem, productVariant.getProduct().getStore());

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

			OrderItem orderItem = OrderItem.createByCartItem(order, cartItem, request);
			Delivery.create(orderItem, cartItem.getProductVariant().getProduct().getStore());
			order.addOrderItem(orderItem);
		}

		orderRepository.save(order);
		return order;
	}

	@Override
	public List<Order> findOrders(Member member) {
		return orderRepository.findByMember(member);
	}

	@Override
	public Order findOrder(Long orderId, Member member) throws CustomException {
		Order order = orderRepository.findById(orderId).orElseThrow(() ->
			new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		return order;
	}

	@Override
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
