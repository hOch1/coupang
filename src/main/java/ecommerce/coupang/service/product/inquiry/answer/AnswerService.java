package ecommerce.coupang.service.product.inquiry.answer;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateAnswerRequest;
import ecommerce.coupang.common.exception.CustomException;

public interface AnswerService {

    /**
     * 문의 답변 등록
     * @param inquiryId 상품 문의 ID
     * @param request 답변 등록 요청 정보
     * @param member 요청한 회원
     * @return 답변
     */
    @LogAction("문의 답변 등록")
    Answer createAnswer(Long inquiryId, Long storeId, CreateAnswerRequest request, Member member) throws CustomException;

    /**
     * 해당 답변 수정
     * @param answerId 답변 ID
     * @param request 답변 수정 요청 정보
     * @param member 요청한 회원
     * @return 답변
     */
    @LogAction("답변 수정")
    Answer updateAnswer(Long answerId, UpdateAnswerRequest request, Member member) throws CustomException;

    /**
     * 해당 답변 삭제
     * @param answerId 답변 ID
     * @param member 요청한 회원
     * @return 답변
     */
    @LogAction("답변 삭제")
    Answer deleteAnswer(Long answerId, Member member) throws CustomException;
}
