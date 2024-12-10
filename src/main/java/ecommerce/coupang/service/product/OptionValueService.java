package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.ProductOptionValue;
import ecommerce.coupang.dto.response.product.OptionResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("OptionValueService")
public interface OptionValueService {

	@LogAction("카테고리별 옵션 조회")
	List<OptionResponse> findCategoryOption(Long categoryId) throws CustomException;

	@LogAction("")
	ProductOptionValue createProductOptionValue(Long optionId) throws CustomException;

	@LogAction("옵션값 조회")
	OptionValue findOptionValue(Long optionId) throws CustomException;
}
