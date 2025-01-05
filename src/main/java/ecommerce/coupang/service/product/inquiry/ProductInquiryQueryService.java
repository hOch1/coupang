package ecommerce.coupang.service.product.inquiry;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.inquiry.AnswerRepository;
import ecommerce.coupang.repository.product.inquiry.ProductInquiryRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductInquiryQueryService {

	private final ProductInquiryRepository productInquiryRepository;
	private final ProductRepository productRepository;
	private final AnswerRepository answerRepository;

	/**
	 * 내가 등록한 문의 목록 조회
	 * @param member 요청한 회원
	 * @return 문의 목록
	 */
	@LogAction("내가 등록한 문의 조회")
	public List<ProductInquiry> findMyInquiries(Member member) {
		return productInquiryRepository.findByMemberId(member.getId());
	}

	/**
	 * 해당 상품 문의 조회
	 * @param productId 상품 ID
	 * @return 문의 리스트
	 */
	@LogAction("해당 상품 문의 조회")
	public List<ProductInquiry> getInquiryByProduct(Long productId) throws CustomException {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		return productInquiryRepository.findByProductIdWithMember(productId);
	}

	/**
	 * 해당 상품의 답변 조회
	 * @param inquiryId 상품 문의 ID
	 * @return 답변
	 */
	@LogAction("문의 답변 조회")
	public Answer findAnswer(Long inquiryId) throws CustomException {
		return answerRepository.findByInquiryId(inquiryId)
			.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
	}
}
