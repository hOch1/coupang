package ecommerce.coupang.service.product.review;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.dto.request.product.review.ReviewSort;
import ecommerce.coupang.repository.product.review.ProductReviewRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductReviewQueryService {

	private final ProductReviewRepository productReviewRepository;

	/**
	 * 내가 작성한 리뷰 목록 조회
	 * @param member 요청한 회원
	 * @return 리뷰 목록
	 */
	@LogAction("나의 리뷰 조회")
	public List<ProductReview> findMyReviews(Member member) {
		return productReviewRepository.findByMemberId(member.getId());
	}

	/**
	 * 해당 상품 리뷰 목록 조회
	 * @param productId 상품 ID
	 * @return 리뷰 리스트
	 */
	@LogAction("상품 리뷰 조회")
	public Page<ProductReview> findReviewsByProduct(Long productId, Integer star, ReviewSort sort, int page, int pageSize) {
		return productReviewRepository.findByProductId(productId, star, sort, PageRequest.of(page, pageSize));
	}
}
