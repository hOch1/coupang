package ecommerce.coupang.repository.product.review;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.product.review.ReviewLike;

@LogLevel("ReviewLikeRepository")
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

	Optional<ReviewLike> findByProductReviewIdAndMemberId(Long reviewId, Long memberId);
}
