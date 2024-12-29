package ecommerce.coupang.service.cart;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.dto.response.cart.CartResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("CartService")
public interface CartService {

	/**
	 * 장바구니에 상품 추가
	 * 이미 해당 상품이 있는 경우 수량만 변경
	 * @param request 추가할 상품 정보
	 * @param member 요청한 회원
	 */
	@LogAction("장바구니 상품 추가")
	Cart addCart(AddCartRequest request, Member member) throws CustomException;

	/**
	 * 내 장바구니 조회
	 * @param member 요청한 회원
	 * @return 장바구니 목록
	 */
	@LogAction("장바구니 조회")
	CartResponse findMyCart(Member member) throws CustomException;

	/**
	 * 장바구니 상푼 수량 변경
	 * @param cartItemId 장바구니 상품 ID
	 * @param quantity 변경할 수량
	 * @param member 요청한 회원
	 * @return 수정한 장바구니 상품 ID
	 */
	@LogAction("장바구니 상품 수량 변경")
	CartItem updateItemQuantity(Long cartItemId, int quantity, Member member) throws CustomException;

	/**
	 * 장바구니 상품 제거
	 * @param cartItemId 장바구니 상품 ID
	 * @param member 요청한 회원
	 * @return 삭제한 장바구니 상품 ID
	 */
	@LogAction("장바구니 상품 제거")
	CartItem removeItem(Long cartItemId, Member member) throws CustomException;

	/**
	 * 장바구니 전체 제거
	 * @param member 요청한 회원
	 */
	@LogAction("장바구니 전체 제거")
	void clearCart(Member member) throws CustomException;
}
