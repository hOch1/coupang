package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.exception.CustomException;

@LogLevel("OptionValueService")
public interface OptionValueService {

	/**
	 * 옵션 값 조회
	 * @param id 옵션 값 ID
	 * @return 옵션 값
	 * @throws CustomException
	 */
	@LogAction("옵션 값 조회")
	OptionValue findOptionValue(Long id) throws CustomException;

	/**
	 * 옵션 타입별 옵션 값 목록 조회
	 * @param categoryOption 조회할 카테고리 옵션 타입
	 * @return 옵션 값 목록
	 */
	@LogAction("옵션 값 목록 조회")
	List<OptionValue> findByCategoryOption(CategoryOption categoryOption);
}
