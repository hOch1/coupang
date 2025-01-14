package ecommerce.coupang.utils.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.domain.category.VariantOption;
import ecommerce.coupang.dto.request.product.option.OptionRequest;

class ProductUtilsTest {

	@Test
	@DisplayName("카테고리 옵션 검증 테스트")
	void validateOptions_CategoryOption() {
		CategoryOption categoryOption = mock(CategoryOption.class);
		when(categoryOption.getId()).thenReturn(1L);

		List<CategoryOption> needOptions = List.of(categoryOption);
		List<OptionRequest> optionRequests = List.of(optionRequest());

		assertDoesNotThrow(() ->ProductUtils.validateOptions(needOptions, optionRequests));
	}

	@Test
	@DisplayName("변형 옵션 검증 테스트")
	void validateOptions_VariantOption() {
		VariantOption variantOption = mock(VariantOption.class);
		when(variantOption.getId()).thenReturn(1L);

		List<VariantOption> needOptions = List.of(variantOption);
		List<OptionRequest> optionRequests = List.of(optionRequest());

		assertDoesNotThrow(() ->ProductUtils.validateOptions(needOptions, optionRequests));

	}

	@Test
	@DisplayName("카테고리 옵션 검증 테스트 - 실패 (누락)")
	void validateOptions_CategoryOption_Fail() {
		CategoryOption categoryOption1 = mock(CategoryOption.class);
		CategoryOption categoryOption2 = mock(CategoryOption.class);
		when(categoryOption1.getId()).thenReturn(1L);
		when(categoryOption2.getId()).thenReturn(2L);

		List<CategoryOption> needOptions = List.of(categoryOption1, categoryOption2);
		List<OptionRequest> optionRequests = List.of(optionRequest());

		CustomException customException = assertThrows(CustomException.class,
			() -> ProductUtils.validateOptions(needOptions, optionRequests));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.OPTION_NOT_CONTAINS);
	}

	@Test
	@DisplayName("변형 옵션 검증 테스트 - 실패 (누락)")
	void validateOptions_VariantOption_Fail() {
		VariantOption variantOption1 = mock(VariantOption.class);
		VariantOption variantOption2 = mock(VariantOption.class);
		when(variantOption1.getId()).thenReturn(1L);
		when(variantOption2.getId()).thenReturn(2L);

		List<VariantOption> needOptions = List.of(variantOption1, variantOption2);
		List<OptionRequest> optionRequests = List.of(optionRequest());

		CustomException customException = assertThrows(CustomException.class,
			() -> ProductUtils.validateOptions(needOptions, optionRequests));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.OPTION_NOT_CONTAINS);
	}

	private OptionRequest optionRequest() {
		return new OptionRequest(
			1L,
			1L
		);
	}
}