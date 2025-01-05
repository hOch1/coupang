package ecommerce.coupang.service.product.inquiry;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.inquiry.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerQueryService {

    private final AnswerRepository answerRepository;

    /**
     * 해당 상품의 답변 조회
     * @param inquiryId 상품 문의 ID
     * @return 답변
     */
    @LogAction("문의 답변 조회")
    public Answer findAnswer(Long inquiryId) throws CustomException {
        return answerRepository.findByInquiryId(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
    }
}
