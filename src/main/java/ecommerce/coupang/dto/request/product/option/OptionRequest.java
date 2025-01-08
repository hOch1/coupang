package ecommerce.coupang.dto.request.product.option;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionRequest {
	private final Long optionId;
	private final Long optionValueId;
}
