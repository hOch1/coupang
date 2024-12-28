package ecommerce.coupang.service.product;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
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
}
