package ecommerce.coupang.service.product;


import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.domain.product.ProductOption;
import ecommerce.coupang.exception.CustomException;

@LogLevel("ProductOptionService")
public interface ProductOptionService {

	/**
	 * 상품 옵션 저장
	 * @param optionId 옵션 값 ID
	 * @param productDetail 상품 상세
	 * @return 저장된 상품 옵션
	 * @throws CustomException
	 */
	@LogAction("상품 옶션 저장")
	ProductOption createProductOption(Long optionId, ProductDetail productDetail) throws CustomException;
}
