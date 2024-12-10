package ecommerce.coupang.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {

	CLOTHES("의류", null, 100, 1),
		MEN("남성의류", CLOTHES, 110, 2),
			MEN_TOP("상의", MEN, 111, 3),
			MEN_BOTTOM("하의", MEN, 112, 3),
			MEN_SHOES("신발", MEN, 113, 3),
		WOMEN("여성의류", CLOTHES, 120, 2),
			WOMEN_TOP("상의", MEN, 121, 3),
			WOMEN_BOTTOM("하의", MEN, 122, 3),
			WOMEN_SHOES("신발", MEN, 123, 3),
		KID("아동", CLOTHES, 130, 2),
			KID_MEN("남아", KID, 131, 3),
			KID_WOMEN("여아", KID, 132, 3),

	FOOD("음식", null, 200, 1),
		MEAT("육류", FOOD, 210, 2),
			MEAT_PIG("돼지고기", MEAT, 211, 3),
			MEAT_COW("소고기", MEAT, 212, 3),
			MEAT_ETC("기타", MEAT, 213, 3),
		SEAFOOD("수산물", FOOD, 220, 2),
			FISH("물고기", SEAFOOD, 221, 3),
			DRY_FISH("건어물", SEAFOOD, 222, 3),
			SHELL_FISH("갑각류", SEAFOOD, 223, 3),
		VEGETABLE("채소", FOOD, 230, 2),
		NUTS("견과류", FOOD, 240, 2),

	HOME("홈", null, 300, 1),
		BED("침구", HOME, 310, 2),
	;

	private final String displayName;
	private final CategoryType parent;
	private final int code;
	private final int level;
}
