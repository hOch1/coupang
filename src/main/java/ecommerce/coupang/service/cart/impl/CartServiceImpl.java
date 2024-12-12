package ecommerce.coupang.service.cart.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.dto.response.cart.CartResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.cart.CartRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.cart.CartService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	@Override
	@Transactional
	public void addCart(AddCartRequest request, Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member).orElseThrow(() ->
			new CustomException(ErrorCode.CART_NOT_FOUND));

		Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
			new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		CartItem existingCartItem = cart.getCartItems()
			.stream()
			.filter(item -> item.getProduct().equals(product))
			.findFirst()
			.orElse(null);

		if (existingCartItem != null)
			existingCartItem.changeQuantity(existingCartItem.getQuantity() + request.getQuantity());
		else {
			CartItem cartItem = CartItem.create(cart, product, request.getQuantity());
			cart.addItem(cartItem);
		}
	}

	@Override
	public CartResponse findMyCart(Member member) throws CustomException {
		return cartRepository.findByMember(member)
			.map(CartResponse::from)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));
	}

	@Override
	@Transactional
	public Long updateItemQuantity(Long cartItemId, int quantity, Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		if (!cartItem.getCart().equals(cart))
			throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);

		cartItem.changeQuantity(quantity);

		return cartItem.getId();
	}

	@Override
	@Transactional
	public Long updateItemQuantity(Long cartItemId, Member member, boolean add) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		if (!cartItem.getCart().equals(cart))
			throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);

		if (add)
			cartItem.addQuantity();
		else
			cartItem.subQuantity();

		return cartItem.getId();
	}

	@Override
	@Transactional
	public Long removeItem(Long cartItemId, Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		cart.getCartItems().remove(cartItem);

		return cartItem.getId();
	}

	@Override
	@Transactional
	public void clearCart(Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		cart.getCartItems().clear();
	}
}
