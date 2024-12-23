package ecommerce.coupang.service.cart.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.cart.CartRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.cart.CartService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductVariantRepository productVariantRepository;

	@Override
	@Transactional
	public Cart addCart(AddCartRequest request, Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		CartItem cartItem = cartItemRepository.findByCartAndProductVariant(cart, productVariant).orElse(null);

		if (cartItem != null)
			cartItem.changeQuantity(request.getQuantity());
		else {
			cartItem = CartItem.create(cart, productVariant, request.getQuantity());
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
		CartItem cartItem = cartItemRepository.findByIdWithMember(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		if (!cartItem.getCart().getMember().equals(member))
			throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);

		cartItem.changeQuantity(quantity);

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
		cartItemRepository.delete(cartItem);

		return cartItem;
	}

	@Override
	@Transactional
	public void clearCart(Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

		cartItemRepository.deleteAll(cart.getCartItems());
		cart.getCartItems().clear();
	}
}
