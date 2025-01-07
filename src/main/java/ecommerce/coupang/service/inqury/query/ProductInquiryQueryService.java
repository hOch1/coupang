package ecommerce.coupang.service.inqury.query;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.inquiry.ProductInquiryRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@LogLevel("ProductInquiryQueryService")
public class ProductInquiryQueryService {

	private final ProductInquiryRepository productInquiryRepository;
	private final ProductRepository productRepository;

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

		/*
		해당 상품 비활성 상태일때 예외
		TODO 예외처리 고민
		 */
		if (!product.isActive())
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);

		return productInquiryRepository.findByProductIdWithMember(productId);
	}
}
