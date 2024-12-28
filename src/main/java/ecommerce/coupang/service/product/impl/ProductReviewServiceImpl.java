package ecommerce.coupang.service.product.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.domain.product.review.ReviewLike;
import ecommerce.coupang.dto.request.product.review.CreateReviewRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.review.ProductReviewRepository;
import ecommerce.coupang.repository.product.review.ReviewLikeRepository;
import ecommerce.coupang.service.product.ProductReviewService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

	private final ProductReviewRepository productReviewRepository;
	private final ProductRepository productRepository;
	private final ReviewLikeRepository reviewLikeRepository;

	@Override
	@Transactional
	public ProductReview createReview(Long productId, CreateReviewRequest request, Member member) throws CustomException {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		// TODO 자기 상품에 리뷰 등록 불가 (테스트시 해제)
		// if (productReviewRepository.existsByProductIdAndMemberId(product.getId(), member.getId()))
		// 	throw new CustomException(ErrorCode.ALREADY_WRITE_REVIEW_PRODUCT);

		ProductReview productReview = ProductReview.create(request, product, member);

		product.addProductReviews(productReview);

		return productReviewRepository.save(productReview);
	}

	@Override
	@Transactional
	public ProductReview likeReview(Long reviewId, Member member) throws CustomException {
		ProductReview productReview = productReviewRepository.findByIdWithMember(reviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

		// TODO 자기 상품엔 리뷰 등록 제한 (테스트시 해제)
		// if (productReview.getMember().equals(member))
		// 	throw new CustomException(ErrorCode.CAN_NOT_LIKE_MY_REVIEW);

		Optional<ReviewLike> findLike = reviewLikeRepository.findByProductReviewIdAndMemberId(productReview.getId(), member.getId());

		if (findLike.isEmpty()) {
			ReviewLike reviewLike = ReviewLike.create(productReview, member);
			productReview.addLikes(reviewLike);
			reviewLikeRepository.save(reviewLike);
		} else {
			ReviewLike reviewLike = findLike.get();
			reviewLikeRepository.delete(reviewLike);
			productReview.getLikes().remove(reviewLike);
		}

		return productReview;
	}
}
