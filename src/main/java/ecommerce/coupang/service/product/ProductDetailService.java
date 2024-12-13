package ecommerce.coupang.service.product;


import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.dto.request.product.CreateProductRequest;

@LogLevel("ProductDetailService")
public interface ProductDetailService {

	/**
	 * 상품 상세 저장
	 * @param request 상품 상세 정보
	 * @param product 해당 상품
	 * @return 저장한 상품 상세
	 */
	@LogAction("상품 상세 저장")
	ProductDetail save(CreateProductRequest.CreateDetailRequest request, Product product);
}
