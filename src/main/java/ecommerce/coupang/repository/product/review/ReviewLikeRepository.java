package ecommerce.coupang.repository.product.review;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.product.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

	Optional<ReviewLike> findByProductReviewIdAndMemberId(Long reviewId, Long memberId);
}
