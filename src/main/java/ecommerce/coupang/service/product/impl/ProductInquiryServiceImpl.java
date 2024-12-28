package ecommerce.coupang.service.product.impl;

import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.repository.product.inquiry.AnswerRepository;
import ecommerce.coupang.repository.store.StoreRepository;
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

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductInquiryServiceImpl implements ProductInquiryService {

	private final ProductInquiryRepository productInquiryRepository;
	private final AnswerRepository answerRepository;
	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;

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

	@Override
	@Transactional
	public Answer createAnswer(Long inquiryId, CreateAnswerRequest request, Member member) throws CustomException {
		ProductInquiry productInquiry = productInquiryRepository.findByIdWithMember(inquiryId)
				.orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

		Store store = storeRepository.findByIdWithMember(request.getStoreId())
				.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (productInquiry.isAnswered())
			throw new CustomException(ErrorCode.ALREADY_ANSWERED);

		if (!store.getMember().equals(member) || !productInquiry.getProduct().getStore().equals(store))
			throw new CustomException(ErrorCode.FORBIDDEN);

		Answer answer = Answer.create(request, productInquiry, store);
		answerRepository.save(answer);

		productInquiry.setAnswer(answer);

		return answer;
	}

	@Override
	public List<ProductInquiry> findMyInquiries(Member member) {
        return productInquiryRepository.findByMemberId(member.getId());
	}

	@Override
	public List<ProductInquiry> getInquiryByProduct(Long productId) throws CustomException {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		return productInquiryRepository.findByProductIdWithMember(productId);
	}
}
