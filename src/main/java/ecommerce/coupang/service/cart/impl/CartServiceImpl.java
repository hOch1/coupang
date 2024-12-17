package ecommerce.coupang.service.cart.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.cart.CartRepository;
import ecommerce.coupang.repository.product.ProductDetailRepository;
import ecommerce.coupang.service.cart.CartService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductDetailRepository productDetailRepository;

	@Override
	@Transactional
	public Cart addCart(AddCartRequest request, Member member) throws CustomException {
		Cart cart = member.getCart();

		ProductDetail productDetail = productDetailRepository.findById(request.getProductDetailId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		CartItem existingCartItem = cart.getCartItems()
			.stream()
			.filter(item -> item.getProductDetail().equals(productDetail))
			.findFirst()
			.orElse(null);

		if (existingCartItem != null)
			existingCartItem.changeQuantity(existingCartItem.getQuantity() + request.getQuantity());
		else {
			CartItem cartItem = CartItem.create(cart, productDetail, request.getQuantity());
			cart.addItem(cartItem);
		}

		return cart;
	}

	@Override
	public Cart findMyCart(Member member) throws CustomException {
		return cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));
	}

	@Override
	@Transactional
	public CartItem updateItemQuantity(Long cartItemId, int quantity, Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		if (!cartItem.getCart().equals(cart))
			throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);

		cartItem.changeQuantity(quantity);

		return cartItem;
	}

	@Override
	@Transactional
	public CartItem updateItemQuantity(Long cartItemId, Member member, boolean add) throws CustomException {
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

		return cartItem;
	}

	@Override
	@Transactional
	public CartItem removeItem(Long cartItemId, Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		cart.getCartItems().remove(cartItem);

		return cartItem;
	}

	@Override
	@Transactional
	public void clearCart(Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		cart.getCartItems().clear();
	}
}
