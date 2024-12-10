package ecommerce.coupang.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryCompany {

	CJ("CJ 대한통운"),
	LOTTE("롯테 택배"),
	HANHWA("힌화 택배"),
	KOR("우체국 택배");

	private final String name;
}
