package ecommerce.coupang.service.cart.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.cart.AddCartRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.cart.CartRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.cart.CartService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final ProductRepository productRepository;

	@Override
	public void addCart(AddCartRequest request, Member member) throws CustomException {
		Cart cart = cartRepository.findByMember(member).orElseThrow(() ->
			new CustomException(ErrorCode.CART_NOT_FOUND));

		Product product = productRepository.findById(request.getProductId()).orElseThrow(() ->
			new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		CartItem cartItem = CartItem.create(cart, product, request.getQuantity());
		cart.addItem(cartItem);
	}
}
