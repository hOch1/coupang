package ecommerce.coupang.service.order.strategy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {
	PRODUCT("orderByProductRequest"),
	CART("orderByCartRequest")
	;

	private final String beanName;
}
