package ecommerce.coupang.dto.request.product.inquiry;

import ecommerce.coupang.domain.product.inquiry.InquiryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateInquiryRequest {

	@NotBlank(message = "뮨의 내용을 입력해주세요.")
	private final String content;

	@NotNull(message = "문의 타입을 선택해주세요.")
	private final InquiryType type;
}
