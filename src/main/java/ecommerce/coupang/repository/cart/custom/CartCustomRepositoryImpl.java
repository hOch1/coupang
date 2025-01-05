package ecommerce.coupang.repository.cart.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ecommerce.coupang.domain.cart.QCart;
import ecommerce.coupang.domain.cart.QCartItem;
import ecommerce.coupang.domain.product.QProduct;
import ecommerce.coupang.domain.product.variant.QProductVariant;
import ecommerce.coupang.domain.product.variant.QProductVariantOption;
import ecommerce.coupang.domain.store.QStore;
import ecommerce.coupang.dto.response.cart.CartResponse;
import ecommerce.coupang.dto.response.option.OptionResponse;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartCustomRepositoryImpl implements CartCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public CartResponse findMyCart(Long memberId) {
        QCart cart = QCart.cart;
        QCartItem cartItem = QCartItem.cartItem;
        QStore store = QStore.store;
        QProductVariant productVariant = QProductVariant.productVariant;
        QProduct product = QProduct.product;
        QProductVariantOption productVariantOption = QProductVariantOption.productVariantOption;

        // CartResponse 조회
        List<CartResponse.CartItemResponse> cartItemResponses = queryFactory
            .select(Projections.constructor(
                CartResponse.CartItemResponse.class,
                cartItem.id,
                Projections.constructor(
                        StoreResponse.class,
                        store.id,
                        store.name
                ),
                productVariant.id,
                product.name,
                productVariant.price,
                cartItem.quantity,
                productVariant.status,
                Projections.list(
                    Projections.constructor(
                        OptionResponse.class,
                        productVariantOption.id,
                        productVariantOption.variantOptionValue.description
                    )
                )
            ))
            .from(cartItem)
            .join(cartItem.productVariant, productVariant)
            .join(productVariant.product, product)
            .join(product.store, store)
            .leftJoin(productVariant.productVariantOptions, productVariantOption)
            .where(cartItem.cart.member.id.eq(memberId)) // 해당 멤버의 장바구니만 조회
            .fetch();

        // Cart 데이터 조회
        Long cartId = queryFactory.select(cart.id)
            .from(cart)
            .where(cart.member.id.eq(memberId))
            .fetchOne();

        int itemCount = cartItemResponses.size();

        // CartResponse 반환
        return new CartResponse(cartId, itemCount, cartItemResponses);
    }
}
