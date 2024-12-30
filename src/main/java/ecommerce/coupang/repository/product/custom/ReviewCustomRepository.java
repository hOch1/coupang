package ecommerce.coupang.repository.product.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.dto.request.product.review.ReviewSort;

public interface ReviewCustomRepository {

	Page<ProductReview> findByProductId(Long productId, Integer star, ReviewSort sort, Pageable pageable);
}
