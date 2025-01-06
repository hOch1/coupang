package ecommerce.coupang.service.product.inquiry;

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
public class ProductInquiryServiceImpl implements ProductInquiryService {

	private final ProductInquiryRepository productInquiryRepository;
	private final ProductRepository productRepository;

	@Override
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

	@Override
	public ProductInquiry updateInquiry(Long inquiryId, UpdateInquiryRequest request, Member member) throws CustomException {
		ProductInquiry productInquiry = getProductInquiry(inquiryId);

		validateInquiryOwner(member, productInquiry);

		productInquiry.update(request);
		return productInquiry;
	}

	@Override
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
