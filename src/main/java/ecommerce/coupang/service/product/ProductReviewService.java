package ecommerce.coupang.service.product;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.domain.product.review.ReviewLike;
import ecommerce.coupang.dto.request.product.review.CreateReviewRequest;
import ecommerce.coupang.dto.request.product.review.UpdateReviewRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.review.ProductReviewRepository;
import ecommerce.coupang.repository.product.review.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@LogLevel("ProductReviewService")
public class ProductReviewService {

	private final ProductReviewRepository productReviewRepository;
	private final ProductRepository productRepository;
	private final ReviewLikeRepository reviewLikeRepository;

	/**
	 * 상품 리뷰 등록
	 * @param productId 상품 ID
	 * @param request 리뷰 등록 요청 정보
	 * @param member 요청한 회원
	 * @return 등록한 상품 리뷰
	 */
	@LogAction("상품 리뷰 등록")
	public ProductReview createReview(Long productId, CreateReviewRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		/*
		 TODO 상품에 리뷰 중복 등록 불가 (테스트시 해제)
		 if (productReviewRepository.existsByProductIdAndMemberId(productId, member.getId()))
		 	throw new CustomException(ErrorCode.ALREADY_WRITE_REVIEW_PRODUCT);

		 TODO 자기 상품에 리뷰 등록 불가 (테스트시 해제)
		 if (product.getStore().getMember().equals(member))
		 	throw new CustomException(ErrorCode.CAN_NOT_WRITE_MY_PRODUCT);
		*/

		ProductReview productReview = ProductReview.create(request, product, member);
		product.addProductReviews(productReview);

		return productReview;
	}

	/**
	 * 상품 리뷰 좋아요
	 * 이미 좋아요한 리뷰면 취소, 아니면 추가
	 * @param reviewId 리뷰 ID
	 * @param member 요청한 회원
	 * @return 좋아요한 리뷰
	 */
	@LogAction("상품 리뷰 좋아요")
	public ProductReview likeReview(Long reviewId, Member member) throws CustomException {
		ProductReview productReview = getProductReview(reviewId);

		/*
		 TODO 자기 리뷰엔 좋아요 제한 (테스트시 해제)
		 if (productReview.getMember().equals(member))
		 	throw new CustomException(ErrorCode.CAN_NOT_LIKE_MY_REVIEW);
		*/

		Optional<ReviewLike> findLike = reviewLikeRepository.findByProductReviewIdAndMemberId(productReview.getId(), member.getId());

		if (findLike.isEmpty()) {
			ReviewLike reviewLike = ReviewLike.create(productReview, member);
			productReview.addLikes(reviewLike);
		} else {
			ReviewLike reviewLike = findLike.get();
			productReview.getLikes().remove(reviewLike);
			productReview.decreaseLikeCount();
		}

		return productReview;
	}


	/**
	 * 리뷰 수정
	 * @param reviewId 수정할 리뷰 ID
	 * @param request 리뷰 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 리뷰
	 */
	@LogAction("리뷰 수정")
	public ProductReview updateReview(Long reviewId, UpdateReviewRequest request, Member member) throws CustomException {
		ProductReview productReview = getProductReview(reviewId);

		productReview.validateReviewOwner(member);

		productReview.update(request);

		return productReview;
	}

	/**
	 * 리뷰 삭제
	 * @param reviewId 삭제할 리뷰 ID
	 * @param member 요청한 회원
	 * @return 삭제한 리뷰
	 */
	@LogAction("리뷰 삭제")
	public ProductReview deleteReview(Long reviewId, Member member) throws CustomException {
		ProductReview productReview = getProductReview(reviewId);

		productReview.validateReviewOwner(member);

		productReview.remove();

		productReviewRepository.delete(productReview);
		return productReview;
	}

	/* 상품 리뷰 가져오기 */
	private ProductReview getProductReview(Long reviewId) throws CustomException {
		return productReviewRepository.findByIdWithMember(reviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
	}
}
