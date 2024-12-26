package ecommerce.coupang.repository.product.custom.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.QCategory;
import ecommerce.coupang.domain.product.QProduct;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.QProductVariant;
import ecommerce.coupang.repository.product.custom.ProductCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

}
