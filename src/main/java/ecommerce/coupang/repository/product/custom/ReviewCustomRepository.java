package ecommerce.coupang.repository.product.custom;

import java.util.List;

import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.dto.request.product.review.ReviewSort;

public interface ReviewCustomRepository {

	List<ProductReview> findByProductId(Long productId, Integer star, ReviewSort sort);
}
