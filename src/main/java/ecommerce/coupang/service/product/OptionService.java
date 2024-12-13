package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.dto.response.product.OptionResponse;
import ecommerce.coupang.exception.CustomException;

public interface OptionService {

	@LogAction("카테고리별 옵션 조회")
	List<OptionResponse> findCategoryOption(Long categoryId) throws CustomException;
}
