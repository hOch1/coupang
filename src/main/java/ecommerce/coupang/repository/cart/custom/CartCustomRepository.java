package ecommerce.coupang.repository.cart.custom;

import ecommerce.coupang.dto.response.cart.CartResponse;

public interface CartCustomRepository {

    CartResponse findMyCart(Long memberId);
}
