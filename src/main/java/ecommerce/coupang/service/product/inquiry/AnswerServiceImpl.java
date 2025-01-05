package ecommerce.coupang.service.product.inquiry;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateAnswerRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.inquiry.AnswerRepository;
import ecommerce.coupang.repository.product.inquiry.ProductInquiryRepository;
import ecommerce.coupang.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final ProductInquiryRepository productInquiryRepository;
    private final StoreService storeService;

    @Override
    public Answer createAnswer(Long inquiryId, Long storeId, CreateAnswerRequest request, Member member) throws CustomException {
        ProductInquiry productInquiry = productInquiryRepository.findByIdWithMember(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));


        if (productInquiry.isAnswered())
            throw new CustomException(ErrorCode.ALREADY_ANSWERED);

        Store store = storeService.validateStoreMember(storeId, member);

        if (!productInquiry.getProduct().getStore().equals(store))
            throw new CustomException(ErrorCode.FORBIDDEN);

        Answer answer = Answer.create(request, productInquiry, store);
        answerRepository.save(answer);

        productInquiry.setAnswer(answer);

        return answer;
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
    public Answer deleteAnswer(Long answerId, Member member) throws CustomException {
        Answer answer = answerRepository.findByIdWithMember(answerId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        if (!answer.getStore().getMember().equals(member))
            throw new CustomException(ErrorCode.FORBIDDEN);

        answerRepository.delete(answer);
        return answer;
    }
}
