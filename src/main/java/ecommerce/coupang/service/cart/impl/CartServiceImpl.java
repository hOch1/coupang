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
import ecommerce.coupang.service.cart.CartQueryService;
import ecommerce.coupang.service.cart.CartService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartQueryService cartQueryService;
	private final CartItemRepository cartItemRepository;
	private final ProductVariantRepository productVariantRepository;

	@Override
	public Cart addCart(AddCartRequest request, Member member) throws CustomException {
		Cart cart = cartQueryService.getCartWithMember(member);

		ProductVariant productVariant = productVariantRepository.findById(request.getProductVariantId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		/*
		장바구니에 해당 상품이 없을시 새로 생성 후 저장
		존재할시 하단 조건문에서 수량추가
		 */
		CartItem cartItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), productVariant.getId())
			.orElseGet(() -> {
				CartItem newCartItem = CartItem.create(cart, productVariant, request.getQuantity());
				cart.addItem(newCartItem);
				return newCartItem;
			});

		if (cartItem.getId() != null)
			cartItem.addQuantity(request.getQuantity());

		return cart;
	}

	@Override
	public CartItem updateItemQuantity(Long cartItemId, int quantity, Member member) throws CustomException {
		CartItem cartItem = cartItemRepository.findByIdWithMember(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		if (!cartItem.getCart().getMember().equals(member))
			throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);

		cartItem.changeQuantity(quantity);

		return cartItem;
	}

	@Override
	public CartItem removeItem(Long cartItemId, Member member) throws CustomException {
		Cart cart = cartQueryService.getCartWithMember(member);

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		cart.getCartItems().remove(cartItem);

		return cartItem;
	}

	@Override
	public void clearCart(Member member) {
		Cart cart = cartQueryService.getCartWithMember(member);

		cart.getCartItems().clear();
	}
}
