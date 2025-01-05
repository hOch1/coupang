package ecommerce.coupang.service.cart.impl;

import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.response.cart.CartResponse;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.cart.CartRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.cart.CartService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductVariantRepository productVariantRepository;
	private final ProductVariantOptionRepository productVariantOptionRepository;

	@Override
	@Transactional
	public Cart addCart(AddCartRequest request, Member member) throws CustomException {
		Cart cart = getCartWithMember(member);

		ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		CartItem cartItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), productVariant.getId())
			.orElseGet(() -> CartItem.create(cart, productVariant, request.getQuantity()));

		if (cartItem.getId() != null)
			cartItem.addQuantity(request.getQuantity());
		else
			cart.addItem(cartItem);

		return cart;
	}

	@Override
	public CartResponse findMyCart(Member member) throws CustomException {
		Cart cart = getCartWithMember(member);

		List<CartItem> cartItems = cartItemRepository.findByMemberIdWithProductStore(member.getId());

		CartResponse response = CartResponse.from(cart);

		for (CartItem cartItem : cartItems) {
			List<ProductVariantOption> productVariantOptions = productVariantOptionRepository.findByProductVariantId(cartItem.getProductVariant().getId());
			CartResponse.CartItemResponse cartItemResponse = CartResponse.CartItemResponse.from(cartItem, productVariantOptions);
			response.addItems(cartItemResponse);
		}

		return response;
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
		Cart cart = getCartWithMember(member);

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		cart.getCartItems().remove(cartItem);
		cartItemRepository.delete(cartItem);

		return cartItem;
	}

	@Override
	@Transactional
	public void clearCart(Member member) throws CustomException {
		Cart cart = getCartWithMember(member);

		cartItemRepository.deleteAll(cart.getCartItems());
		cart.getCartItems().clear();
	}

	private Cart getCartWithMember(Member member) throws CustomException {
		return cartRepository.findByMemberId(member.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));
	}
}
