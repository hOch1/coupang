package ecommerce.coupang.service.product.inquiry;

import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateInquiryRequest;
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
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductInquiryServiceImpl implements ProductInquiryService {

	private final ProductInquiryRepository productInquiryRepository;
	private final AnswerRepository answerRepository;
	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;

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
	public Answer createAnswer(Long inquiryId, Long storeId, CreateAnswerRequest request, Member member) throws CustomException {
		ProductInquiry productInquiry = productInquiryRepository.findByIdWithMember(inquiryId)
				.orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

		Store store = storeRepository.findByIdWithMember(storeId)
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
	public ProductInquiry updateInquiry(Long inquiryId, UpdateInquiryRequest request, Member member) throws CustomException {
		ProductInquiry productInquiry = productInquiryRepository.findByIdWithMember(inquiryId)
			.orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

		if (!productInquiry.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		productInquiry.update(request);
		return productInquiry;
	}

	@Override
	public Answer updateAnswer(Long answerId, UpdateAnswerRequest request, Member member) throws CustomException {
		Answer answer = answerRepository.findByIdWithMember(answerId)
			.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

		if (!answer.getStore().getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		answer.update(request);
		return answer;
	}

	@Override
	public ProductInquiry deleteInquiry(Long inquiryId, Member member) throws CustomException {
		ProductInquiry productInquiry = productInquiryRepository.findByIdWithMember(inquiryId)
			.orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

		if (!productInquiry.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		productInquiryRepository.delete(productInquiry);
		return productInquiry;
	}

	@Override
	public Answer deleteAnswer(Long answerId, Member member) throws CustomException {
		Answer answer = answerRepository.findByIdWithMember(answerId)
			.orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

		if (!answer.getStore().getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		answerRepository.delete(answer);
		return answer;
	}
}
