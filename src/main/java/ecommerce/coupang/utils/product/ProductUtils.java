package ecommerce.coupang.utils.product;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;

public class ProductUtils {

	public static <T, R> void validateOptions(
		List<T> needOptions,
		Collection<R> requestOptions,
		Function<T, Long> needOptionsIdMapper,
		Function<R, Long> requestOptionsIdMapper) throws CustomException {

		Set<Long> needOptionsIdSet = needOptions.stream()
			.map(needOptionsIdMapper)
			.collect(Collectors.toSet());

		requestOptions.stream()
			.map(requestOptionsIdMapper)
			.forEach(needOptionsIdSet::remove);

		if (!needOptionsIdSet.isEmpty())
			throw new CustomException(ErrorCode.OPTION_NOT_CONTAINS); // TODO 예외 구체화
	}
}
