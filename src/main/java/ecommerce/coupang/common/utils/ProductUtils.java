package ecommerce.coupang.common.utils;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;

public class ProductUtils {

	public static void verifyStatusAndReduceStock(ProductVariant productVariant, int quantity) throws CustomException {
		if (!productVariant.getStatus().equals(ProductStatus.ACTIVE))
			throw new CustomException(ErrorCode.PRODUCT_STATUS_NOT_ACTIVE);

		productVariant.reduceStock(quantity);
	}
}
