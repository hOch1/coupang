package ecommerce.coupang.service.product.inquiry.answer;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateAnswerRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.inquiry.AnswerRepository;
import ecommerce.coupang.repository.product.inquiry.ProductInquiryRepository;
import ecommerce.coupang.common.utils.StoreUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final ProductInquiryRepository productInquiryRepository;

    @Override
    public Answer createAnswer(Long inquiryId, Long storeId, CreateAnswerRequest request, Member member) throws CustomException {
        ProductInquiry productInquiry = productInquiryRepository.findByIdWithMember(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        Store store = productInquiry.getProduct().getStore();
        StoreUtils.validateStoreOwner(store, member);

        /* 해당 문의 이미 답변완료일 시 예외 */
        if (productInquiry.isAnswered())
            throw new CustomException(ErrorCode.ALREADY_ANSWERED);

        Answer answer = Answer.create(request, productInquiry, store);
        answerRepository.save(answer);

        productInquiry.setAnswer(answer);

        return answer;
    }

    @Override
    public Answer updateAnswer(Long answerId, UpdateAnswerRequest request, Member member) throws CustomException {
        Answer answer = getAnswer(answerId);

        StoreUtils.validateStoreOwner(answer.getStore(), member);

        answer.update(request);
        return answer;
    }

    @Override
    public Answer deleteAnswer(Long answerId, Member member) throws CustomException {
        Answer answer = getAnswer(answerId);

        StoreUtils.validateStoreOwner(answer.getStore(), member);

        answerRepository.delete(answer);
        return answer;
    }

    /* 답변 가져오기 */
    private Answer getAnswer(Long answerId) throws CustomException {
        return answerRepository.findByIdWithMember(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
    }
}
