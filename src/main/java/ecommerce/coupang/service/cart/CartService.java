package ecommerce.coupang.service.cart;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;

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
import ecommerce.coupang.repository.product.ProductVariantRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@LogLevel("CartService")
public class CartService {

	private final CartQueryService cartQueryService;
	private final CartItemRepository cartItemRepository;
	private final ProductVariantRepository productVariantRepository;

	/**
	 * 장바구니에 상품 추가
	 * 이미 해당 상품이 있는 경우 수량만 변경
	 * @param request 추가할 상품 정보
	 * @param member 요청한 회원
	 */
	@LogAction("장바구니 상품 추가")
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

	/**
	 * 장바구니 상푼 수량 변경
	 * @param cartItemId 장바구니 상품 ID
	 * @param quantity 변경할 수량
	 * @param member 요청한 회원
	 * @return 수정한 장바구니 상품 ID
	 */
	@LogAction("장바구니 상품 수량 변경")
	public CartItem updateItemQuantity(Long cartItemId, int quantity, Member member) throws CustomException {
		CartItem cartItem = cartItemRepository.findByIdWithMember(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		if (!cartItem.getCart().getMember().equals(member))
			throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);

		cartItem.changeQuantity(quantity);

		return cartItem;
	}

	/**
	 * 장바구니 상품 제거
	 * @param cartItemId 장바구니 상품 ID
	 * @param member 요청한 회원
	 * @return 삭제한 장바구니 상품 ID
	 */
	@LogAction("장바구니 상품 제거")
	public CartItem removeItem(Long cartItemId, Member member) throws CustomException {
		Cart cart = cartQueryService.getCartWithMember(member);

		CartItem cartItem = cartItemRepository.findById(cartItemId)
			.orElseThrow(() -> new CustomException(ErrorCode.CART_ITEM_NOT_FOUND));

		cart.getCartItems().remove(cartItem);

		return cartItem;
	}

	/**
	 * 장바구니 전체 제거
	 * @param member 요청한 회원
	 */
	@LogAction("장바구니 전체 제거")
	public void clearCart(Member member) {
		Cart cart = cartQueryService.getCartWithMember(member);

		cart.getCartItems().clear();
	}
}
