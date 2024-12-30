package ecommerce.coupang.service.product;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateInquiryRequest;
import ecommerce.coupang.exception.CustomException;

import java.util.List;

@LogLevel("ProductInquiryService")
public interface ProductInquiryService {

	/**
	 * 상품 문의 등록
	 * @param productId 등록할 상품 ID
	 * @param request 상품 문의 등록 요청 정보
	 * @param member 요청한 회원
	 * @return 상품 문의
	 */
	@LogAction("상품 문의 등록")
	ProductInquiry createInquiry(Long productId, CreateInquiryRequest request, Member member) throws CustomException;

	/**
	 * 문의 답변 등록
	 * @param inquiryId 상품 문의 ID
	 * @param request 답변 등록 요청 정보
	 * @param member 요청한 회원
	 * @return 답변
	 */
	@LogAction("문의 답변 등록")
    Answer createAnswer(Long inquiryId, Long storeId, CreateAnswerRequest request, Member member) throws CustomException;

	/**
	 * 내가 등록한 문의 목록 조회
	 * @param member 요청한 회원
	 * @return 문의 목록
	 */
	@LogAction("내가 등록한 문의 조회")
	List<ProductInquiry> findMyInquiries(Member member);

	/**
	 * 해당 상품 문의 조회
	 * @param productId 상품 ID
	 * @return 문의 리스트
	 */
	@LogAction("해당 상품 문의 조회")
	List<ProductInquiry> getInquiryByProduct(Long productId) throws CustomException;

	/**
	 * 해당 상품의 답변 조회
	 * @param inquiryId 상품 문의 ID
	 * @return 답변
	 */
	@LogAction("문의 답변 조회")
	Answer findAnswer(Long inquiryId) throws CustomException;

	/**
	 * 해당 문의 수정
	 * @param inquiryId 문의 ID
	 * @param request 문의 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 문의
	 */
	@LogAction("문의 수정")
	ProductInquiry updateInquiry(Long inquiryId, UpdateInquiryRequest request, Member member) throws CustomException;

	/**
	 * 해당 답변 수정
	 * @param answerId 답변 ID
	 * @param request 답변 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 답변
	 */
	@LogAction("답변 수정")
	Answer updateAnswer(Long answerId, UpdateAnswerRequest request, Member member) throws CustomException;

	/**
	 * 해당 문의 삭제
	 * @param inquiryId 문의 ID
	 * @param member 요청한 회원
	 * @return 문의
	 */
	@LogAction("문의 삭제")
	ProductInquiry deleteInquiry(Long inquiryId, Member member) throws CustomException;

	/**
	 * 해당 답변 삭제
	 * @param answerId 답변 ID
	 * @param member 요청한 회원
	 * @return 답변
	 */
	@LogAction("답변 삭제")
	Answer deleteAnswer(Long answerId, Member member) throws CustomException;
}
