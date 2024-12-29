package ecommerce.coupang.dto.response.product.inquiry;

import java.time.LocalDateTime;

import ecommerce.coupang.domain.product.inquiry.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerResponse {

	private final Long id;
	private final Long storeId;
	private final String StoreName;
	private final String answer;
	private final LocalDateTime createdAt;

	public static AnswerResponse from(Answer answer) {
		return new AnswerResponse(
			answer.getId(),
			answer.getStore().getId(),
			answer.getStore().getName(),
			answer.getAnswer(),
			answer.getCreatedAt()
		);
	}
}
