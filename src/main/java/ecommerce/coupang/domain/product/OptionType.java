package ecommerce.coupang.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionType {
	//의류 옵션
	SIZE("사이즈", CategoryType.CLOTHES),
	TEXTURE("재질", CategoryType.CLOTHES),
	FIT("핏", CategoryType.CLOTHES),
	COLOR("색상", CategoryType.CLOTHES),
	KID_AGE("아동 나이", CategoryType.KID),

	//음식 옵션
	DOMESTIC("국내산", CategoryType.FOOD),

	//홈 옵션
	MATERIAL("원자재", CategoryType.HOME)
	;

	private final String displayName;
	private final CategoryType categoryType;
}
