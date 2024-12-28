package ecommerce.coupang.dto.request.product.inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAnswerRequest {

    private final Long storeId;
    private final String answer;
}
