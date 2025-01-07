package ecommerce.coupang.service.inqury;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
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
@LogLevel("AnswerService")
public class AnswerService{

    private final AnswerRepository answerRepository;
    private final ProductInquiryRepository productInquiryRepository;

    /**
     * 문의 답변 등록
     * @param inquiryId 상품 문의 ID
     * @param request 답변 등록 요청 정보
     * @param member 요청한 회원
     * @return 답변
     */
    @LogAction("문의 답변 등록")
    public Answer createAnswer(Long inquiryId, CreateAnswerRequest request, Member member) throws CustomException {
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

    /**
     * 해당 답변 수정
     * @param answerId 답변 ID
     * @param request 답변 수정 요청 정보
     * @param member 요청한 회원
     * @return 답변
     */
    @LogAction("답변 수정")
    public Answer updateAnswer(Long answerId, UpdateAnswerRequest request, Member member) throws CustomException {
        Answer answer = getAnswer(answerId);

        StoreUtils.validateStoreOwner(answer.getStore(), member);

        answer.update(request);
        return answer;
    }

    /**
     * 해당 답변 삭제
     * @param answerId 답변 ID
     * @param member 요청한 회원
     * @return 답변
     */
    @LogAction("답변 삭제")
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
