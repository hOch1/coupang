package ecommerce.coupang.dto.response.product.inquiry;

import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InquiryResponse {

    private final Long id;
    private final Long productId;
    private final String content;
    private final boolean isAnswered;
    private final LocalDateTime createdAt;

    public static InquiryResponse from(ProductInquiry productInquiry) {
        return new InquiryResponse(
                productInquiry.getId(),
                productInquiry.getProduct().getId(),
                productInquiry.getContent(),
                productInquiry.isAnswered(),
                productInquiry.getCreatedAt()
        );
    }
}
