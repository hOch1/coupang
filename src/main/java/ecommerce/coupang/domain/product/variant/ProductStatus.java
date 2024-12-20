package ecommerce.coupang.domain.product.variant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductStatus {
	ACTIVE("정상"),
	INACTIVE("중지"),
	NO_STOCK("재고없음")
	;


	private final String displayName;
}
