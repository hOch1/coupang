package ecommerce.coupang.dto.request.product.inquiry;

import ecommerce.coupang.domain.product.inquiry.InquiryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateInquiryRequest {

	private final String content;
	private final InquiryType type;

}
