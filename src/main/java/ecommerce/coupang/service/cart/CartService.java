package ecommerce.coupang.service.cart;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.exception.CustomException;

@LogLevel("CartService")
public interface CartService {

	/**
	 * 장바구니에 상품 추가
	 * @param request 추가할 상품 정보
	 * @param member 요청한 회원
	 */
	void addCart(AddCartRequest request, Member member) throws CustomException;
}
