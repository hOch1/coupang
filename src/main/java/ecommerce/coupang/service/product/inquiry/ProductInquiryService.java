package ecommerce.coupang.service.product.inquiry;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateInquiryRequest;
import ecommerce.coupang.exception.CustomException;

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
	 * 해당 문의 수정
	 * @param inquiryId 문의 ID
	 * @param request 문의 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 문의
	 */
	@LogAction("문의 수정")
	ProductInquiry updateInquiry(Long inquiryId, UpdateInquiryRequest request, Member member) throws CustomException;

	/**
	 * 해당 문의 삭제
	 * @param inquiryId 문의 ID
	 * @param member 요청한 회원
	 * @return 문의
	 */
	@LogAction("문의 삭제")
	ProductInquiry deleteInquiry(Long inquiryId, Member member) throws CustomException;
}
