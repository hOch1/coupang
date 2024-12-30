package ecommerce.coupang.repository.product.review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.repository.product.custom.ReviewCustomRepository;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, ReviewCustomRepository {

	@Query("select pr from ProductReview pr "
		+ "join fetch pr.member m "
		+ "where pr.id = :productReviewId ")
	Optional<ProductReview> findByIdWithMember(Long productReviewId);

	@Query("select pr from ProductReview pr "
		+ "join fetch pr.member m " 
		+ "join fetch m.cart c "
		+ "where m.id = :memberId")
	List<ProductReview> findByMemberId(Long memberId);
}
