package ecommerce.coupang.service.cart;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

	@InjectMocks
	private CartService cartService;

	@Mock
	private CartQueryService cartQueryService;

	@Mock
	private CartItemRepository cartItemRepository;

	@Mock
	private ProductVariantRepository productVariantRepository;

	private Cart cart = mock(Cart.class);
	private CartItem cartItem = mock(CartItem.class);
	private ProductVariant productVariant = mock(ProductVariant.class);
	private Member member = mock(Member.class);

	@Test
	@DisplayName("장바구니 추가 (새로운 상품)")
	void addCart_new() throws CustomException {
		when(cart.getId()).thenReturn(1L);
		when(productVariant.getId()).thenReturn(1L);
		AddCartRequest request = addCartRequest();

		when(cartQueryService.getCartWithMember(member)).thenReturn(cart);
		when(productVariantRepository.findById(request.getProductVariantId())).thenReturn(Optional.of(productVariant));
		when(cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), productVariant.getId())).thenReturn(Optional.empty());

		cartService.addCart(request, member);

		verify(cartQueryService).getCartWithMember(member);
		verify(productVariantRepository).findById(request.getProductVariantId());
		verify(cartItemRepository).findByCartIdAndProductVariantId(cart.getId(), productVariant.getId());
		verify(cart).addItem(any(CartItem.class));
		verify(cartItem, never()).addQuantity(request.getQuantity());
	}

	@Test
	@DisplayName("장바구니 추가 (기존 상품 있음)")
	void addCart_exits() throws CustomException {
		when(cart.getId()).thenReturn(1L);
		when(productVariant.getId()).thenReturn(1L);
		AddCartRequest request = addCartRequest();

		when(cartQueryService.getCartWithMember(member)).thenReturn(cart);
		when(productVariantRepository.findById(request.getProductVariantId())).thenReturn(Optional.of(productVariant));
		when(cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), productVariant.getId())).thenReturn(Optional.of(cartItem));

		cartService.addCart(request, member);

		verify(cartQueryService).getCartWithMember(member);
		verify(productVariantRepository).findById(request.getProductVariantId());
		verify(cartItemRepository).findByCartIdAndProductVariantId(cart.getId(), productVariant.getId());
		verify(cart, never()).addItem(cartItem);
		verify(cartItem).addQuantity(request.getQuantity());
	}

	@Test
	@DisplayName("장바구니 추가 - 실패 (상품 못찾음)")
	void addCart_fail_ProductNotFound() {
		when(cart.getId()).thenReturn(1L);
		AddCartRequest request = addCartRequest();

		when(cartQueryService.getCartWithMember(member)).thenReturn(cart);
		when(productVariantRepository.findById(request.getProductVariantId())).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> cartService.addCart(request, member));

		verify(cartQueryService).getCartWithMember(member);
		verify(productVariantRepository).findById(request.getProductVariantId());
		verify(cartItemRepository, never()).findByCartIdAndProductVariantId(cart.getId(), productVariant.getId());
		verify(cart, never()).addItem(cartItem);
		verify(cartItem, never()).addQuantity(request.getQuantity());

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	@Test
	@DisplayName("수량 변경")
	void updateItemQuantity() throws CustomException {
		Long cartItemId = 1L;
		int quantity = 1;

		when(cartItem.getCart()).thenReturn(cart);
		when(cart.getMember()).thenReturn(member);
		when(cartItemRepository.findByIdWithMember(cartItemId)).thenReturn(Optional.of(cartItem));

		cartService.updateItemQuantity(cartItemId, quantity, member);

		verify(cartItemRepository).findByIdWithMember(cartItemId);
		verify(cartItem).changeQuantity(quantity);
	}

	@Test
	@DisplayName("수량 변경 - 실패 (장바구니 상품 못찾음)")
	void updateItemQuantity_fail_cartItemNotfound() throws CustomException {
		Long cartItemId = 1L;
		int quantity = 1;

		when(cartItemRepository.findByIdWithMember(cartItemId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> cartService.updateItemQuantity(cartItemId, quantity, member));

		verify(cartItemRepository).findByIdWithMember(cartItemId);
		verify(cartItem, never()).changeQuantity(quantity);

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND);
	}

	@Test
	@DisplayName("수량 변경 - 실패 (회원이 장바구니 주인이 아님)")
	void updateItemQuantity_fail_MemberNotOwnerCart() throws CustomException {
		Long cartItemId = 1L;
		int quantity = 1;
		Member otherMember = mock(Member.class);

		when(cartItem.getCart()).thenReturn(cart);
		when(cart.getMember()).thenReturn(member);
		when(cartItemRepository.findByIdWithMember(cartItemId)).thenReturn(Optional.of(cartItem));

		CustomException customException = assertThrows(CustomException.class,
			() -> cartService.updateItemQuantity(cartItemId, quantity, otherMember));

		verify(cartItemRepository).findByIdWithMember(cartItemId);
		verify(cartItem, never()).changeQuantity(quantity);

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
	}

	@Test
	@DisplayName("장바구니 상품 제거")
	void removeItem() throws CustomException {
		Long cartItemId = 1L;

		when(cartQueryService.getCartWithMember(member)).thenReturn(cart);
		when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

		cartService.removeItem(cartItemId, member);

		verify(cartQueryService).getCartWithMember(member);
		verify(cartItemRepository).findById(cartItemId);
		verify(cart).removeItem(cartItem);
	}

	@Test
	@DisplayName("장바구니 상품 제거 - 실패 (장바구니 상품 못찾음)")
	void removeItem_fail_cartItemNotFound() {
		Long cartItemId = 1L;

		when(cart.getCartItems()).thenReturn(new ArrayList<>(List.of(cartItem)));
		when(cartQueryService.getCartWithMember(member)).thenReturn(cart);
		when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> cartService.removeItem(cartItemId, member));

		verify(cartQueryService).getCartWithMember(member);
		verify(cartItemRepository).findById(cartItemId);
		verify(cart, never()).removeItem(cartItem);

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.CART_ITEM_NOT_FOUND);
	}

	@Test
	@DisplayName("장바구니 상품 전체 제거")
	void clearCart() {
		when(cartQueryService.getCartWithMember(member)).thenReturn(cart);

		cartService.clearCart(member);

		verify(cart).clear();
	}

	private AddCartRequest addCartRequest() {
		return new AddCartRequest(
			1L,
			1
		);
	}
}