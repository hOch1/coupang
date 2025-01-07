package ecommerce.coupang.controller.product;

import ecommerce.coupang.domain.product.inquiry.Answer;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateAnswerRequest;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.product.inquiry.AnswerResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.security.CustomUserDetails;
import ecommerce.coupang.service.product.AnswerService;
import ecommerce.coupang.service.product.query.AnswerQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/answers")
@RequiredArgsConstructor
@Tag(name = "상품 답변 API V1", description = "상품 답변관련 API")
public class AnswerController {

    private final AnswerService answerService;
    private final AnswerQueryService answerQueryService;

    @PostMapping("/{inquiryId}")
    @Operation(summary = "상품 문의의 답변 등록 API", description = "상품 문의에 대한 답변을 등록합니다.")
    public ResponseEntity<Void> createAnswer(
            @PathVariable Long inquiryId,
            @RequestBody @Valid CreateAnswerRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

        answerService.createAnswer(inquiryId, request, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{inquiryId}")
    @Operation(summary = "답변 조회 API", description = "해당 상품 문의 답변을 조회합니다")
    public ResponseEntity<Result<AnswerResponse>> findInquiry(
            @PathVariable Long inquiryId) throws CustomException {

        Answer answer = answerQueryService.findAnswer(inquiryId);
        return ResponseEntity.ok(new Result<>(AnswerResponse.from(answer)));
    }

    @PatchMapping("/{answerId}")
    @Operation(summary = "답변 수정 API", description = "해당 답변을 수정합니다")
    public ResponseEntity<Void> updateInquiry(
            @PathVariable Long answerId,
            @RequestBody UpdateAnswerRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

        answerService.updateAnswer(answerId, request, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{answerId}")
    @Operation(summary = "답변 삭제 API", description = "해당 답변을 삭제합니다")
    public ResponseEntity<Void> deleteAnswer(
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

        answerService.deleteAnswer(answerId, userDetails.getMember());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
