package ecommerce.coupang.utils.product;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.category.Option;
import ecommerce.coupang.dto.request.product.option.OptionRequest;

public final class ProductUtils {
	public static <N extends Option> void validateOptions(
		List<N> needOptions,
		List<OptionRequest> requestOptions) throws CustomException {

		Set<Long> needOptionsIdSet = needOptions.stream()
			.map(Option::getId)
			.collect(Collectors.toSet());

		requestOptions.stream()
			.map(OptionRequest::getOptionId)
			.forEach(needOptionsIdSet::remove);

		if (!needOptionsIdSet.isEmpty())
			throw new CustomException(ErrorCode.OPTION_NOT_CONTAINS); // TODO 예외 구체화
	}
}
