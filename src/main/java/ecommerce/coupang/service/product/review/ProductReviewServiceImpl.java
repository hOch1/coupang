package ecommerce.coupang.service.product.review;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ProductReviewServiceImpl implements ProductReviewService {

	private final ProductReviewRepository productReviewRepository;
	private final ProductRepository productRepository;
	private final ReviewLikeRepository reviewLikeRepository;

	@Override
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

		return productReviewRepository.save(productReview);
	}

	@Override
	public ProductReview likeReview(Long reviewId, Member member) throws CustomException {
		ProductReview productReview = productReviewRepository.findByIdWithMember(reviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

		/*
		 TODO 자기 리뷰엔 좋아요 제한 (테스트시 해제)
		 if (productReview.getMember().equals(member))
		 	throw new CustomException(ErrorCode.CAN_NOT_LIKE_MY_REVIEW);
		*/

		Optional<ReviewLike> findLike = reviewLikeRepository.findByProductReviewIdAndMemberId(productReview.getId(), member.getId());

		if (findLike.isEmpty()) {
			ReviewLike reviewLike = ReviewLike.create(productReview, member);
			productReview.addLikes(reviewLike);
			reviewLikeRepository.save(reviewLike);
		} else {
			ReviewLike reviewLike = findLike.get();
			reviewLikeRepository.delete(reviewLike);
			productReview.getLikes().remove(reviewLike);
			productReview.decreaseLikeCount();
		}

		return productReview;
	}


	@Override
	public ProductReview updateReview(Long reviewId, UpdateReviewRequest request, Member member) throws CustomException {
		ProductReview productReview = productReviewRepository.findByIdWithMember(reviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

		if (!productReview.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		productReview.update(request);
		Product product = productReview.getProduct();
		product.updateStarAvg();

		productRepository.save(product);
		return productReview;
	}

	@Override
	public ProductReview deleteReview(Long reviewId, Member member) throws CustomException {
		ProductReview productReview = productReviewRepository.findByIdWithMember(reviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

		if (!productReview.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		Product product = productReview.getProduct();
		product.getProductReviews().remove(productReview);
		product.updateStarAvg();
		product.decreaseReviewCount();

		productRepository.save(product);
		return productReview;
	}
}
