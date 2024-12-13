package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.exception.CustomException;

public interface OptionValueService {

	OptionValue findOptionValue(Long id) throws CustomException;
	List<OptionValue> findByCategoryOption(CategoryOption categoryOption);
}
