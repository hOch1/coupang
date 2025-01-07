package ecommerce.coupang.service.inqury;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.dto.request.product.inquiry.UpdateInquiryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.inquiry.ProductInquiryRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@LogLevel("ProductInquiryService")
public class ProductInquiryService {

	private final ProductInquiryRepository productInquiryRepository;
	private final ProductRepository productRepository;

	/**
	 * 상품 문의 등록
	 * @param productId 등록할 상품 ID
	 * @param request 상품 문의 등록 요청 정보
	 * @param member 요청한 회원
	 * @return 상품 문의
	 */
	@LogAction("상품 문의 등록")
	public ProductInquiry createInquiry(Long productId, CreateInquiryRequest request, Member member) throws CustomException {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		/*
		TODO 자신의 상품엔 문의 등록 불가 (테스트시 해제)
		if (product.getStore().getMember().equals(member))
			throw new CustomException(ErrorCode.CAN_NOT_INQUIRY_MY_PRODUCT);
		 */

		ProductInquiry productInquiry = ProductInquiry.create(request, product, member);
		productInquiryRepository.save(productInquiry);

		return productInquiry;
	}

	/**
	 * 해당 문의 수정
	 * @param inquiryId 문의 ID
	 * @param request 문의 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 문의
	 */
	@LogAction("문의 수정")
	public ProductInquiry updateInquiry(Long inquiryId, UpdateInquiryRequest request, Member member) throws CustomException {
		ProductInquiry productInquiry = getProductInquiry(inquiryId);

		validateInquiryOwner(member, productInquiry);

		productInquiry.update(request);
		return productInquiry;
	}

	/**
	 * 해당 문의 삭제
	 * @param inquiryId 문의 ID
	 * @param member 요청한 회원
	 * @return 문의
	 */
	@LogAction("문의 삭제")
	public ProductInquiry deleteInquiry(Long inquiryId, Member member) throws CustomException {
		ProductInquiry productInquiry = getProductInquiry(inquiryId);

		validateInquiryOwner(member, productInquiry);

		productInquiryRepository.delete(productInquiry);
		return productInquiry;
	}

	/* 해당 문의 작성자가 member 가 맞는지 검증 */
	private static void validateInquiryOwner(Member member, ProductInquiry productInquiry) throws CustomException {
		if (!productInquiry.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}

	/* 문의 가져오기 */
	private ProductInquiry getProductInquiry(Long inquiryId) throws CustomException {
		return productInquiryRepository.findByIdWithMember(inquiryId)
			.orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));
	}
}
