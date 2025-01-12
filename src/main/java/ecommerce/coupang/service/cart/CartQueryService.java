package ecommerce.coupang.service.cart;

import ecommerce.coupang.domain.cart.Cart;
import ecommerce.coupang.domain.cart.CartItem;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.response.cart.CartResponse;
import ecommerce.coupang.repository.cart.CartItemRepository;
import ecommerce.coupang.repository.cart.CartRepository;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartQueryService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantOptionRepository productVariantOptionRepository;

    public CartResponse findMyCart(Member member) {
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

    public Cart getCartWithMember(Member member) {
        return cartRepository.findByMemberId(member.getId()).orElseGet(() -> {
            Cart newCart = Cart.from(member);
            return cartRepository.save(newCart);
        });
    }
}
