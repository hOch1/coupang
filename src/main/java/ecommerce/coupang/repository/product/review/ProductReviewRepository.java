package ecommerce.coupang.repository.product.review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.product.review.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

	@Query("select pr from ProductReview pr "
		+ "join fetch pr.member m "
		+ "where pr.id = :productReviewId ")
	Optional<ProductReview> findByIdWithMember(Long productReviewId);

	boolean existsByProductIdAndMemberId(Long id, Long id1);

	List<ProductReview> findByProductId(Long id);

	@Query("select pr from ProductReview pr "
		+ "join fetch pr.member m " 
		+ "join fetch m.cart c "
		+ "where m.id = :memberId")
	List<ProductReview> findByMemberId(Long memberId);
}
