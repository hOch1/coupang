package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.ProductOptionValue;
import ecommerce.coupang.exception.CustomException;

@LogLevel("ProductOptionService")
public interface ProductOptionService {

	@LogAction("")
	ProductOptionValue createProductOptionValue(OptionValue optionValue) throws CustomException;
}
