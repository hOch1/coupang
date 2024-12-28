package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.dto.request.product.review.CreateReviewRequest;
import ecommerce.coupang.dto.request.product.review.UpdateReviewRequest;
import ecommerce.coupang.exception.CustomException;

@LogLevel("ProductReviewService")
public interface ProductReviewService {

	/**
	 * 상품 리뷰 등록
	 * @param productId 상품 ID
	 * @param request 리뷰 등록 요청 정보
	 * @param member 요청한 회원
	 * @return 등록한 상품 리뷰
	 */
	@LogAction("상품 리뷰 등록")
	ProductReview createReview(Long productId, CreateReviewRequest request, Member member) throws CustomException;

	/**
	 * 상품 리뷰 좋아요
	 * 이미 좋아요한 리뷰면 취소, 아니면 추가
	 * @param reviewId 리뷰 ID
	 * @param member 요청한 회원
	 * @return 좋아요한 리뷰
	 */
	@LogAction("상품 리뷰 좋아요")
	ProductReview likeReview(Long reviewId, Member member) throws CustomException;

	/**
	 * 내가 작성한 리뷰 목록 조회
	 * @param member 요청한 회원
	 * @return 리뷰 목록
	 */
	@LogAction("나의 리뷰 조회")
	List<ProductReview> findMyReviews(Member member);

	/**
	 * 리뷰 수정
	 * @param reviewId 수정할 리뷰 ID
	 * @param request 리뷰 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 리뷰
	 */
	@LogAction("리뷰 수정")
	ProductReview updateReview(Long reviewId, UpdateReviewRequest request, Member member) throws CustomException;

	/**
	 * 리뷰 삭제
	 * @param reviewId 삭제할 리뷰 ID
	 * @param member 요청한 회원
	 * @return 삭제한 리뷰
	 */
	ProductReview deleteReview(Long reviewId, Member member) throws CustomException;
}
