package ecommerce.coupang.service.product.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.inquiry.ProductInquiryRepository;
import ecommerce.coupang.service.product.ProductInquiryService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductInquiryServiceImpl implements ProductInquiryService {

	private final ProductInquiryRepository productInquiryRepository;
	private final ProductRepository productRepository;

	@Override
	@Transactional
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
}
