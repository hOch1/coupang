package ecommerce.coupang.dto.request.product.inquiry;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateInquiryRequest {

	@NotBlank(message = "수정할 문의 내용을 입력해주세요.")
	private final String content;
}
