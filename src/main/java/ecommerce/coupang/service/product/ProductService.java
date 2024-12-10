package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.exception.CustomException;

@LogLevel("ProductService")
public interface ProductService {

	/**
	 * 상품 등록
	 * @param request 상품 추가 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 상품 ID
	 * @throws CustomException
	 */
	@LogAction("상품 등록")
	Long createProduct(CreateProductRequest request, Member member) throws CustomException;

	/**
	 * 상품 정보 변경
	 * @param request 상품 변경 요청 정보
	 * @param member 요청한 회원
	 */
	@LogAction("상품 수정")
	Long updateProduct(UpdateProductRequest request, Long productId, Member member) throws CustomException;

	/**
	 * 카테고리별 상품 조회 (하위 카테고리 포함)
	 * @return 상품 리스트
	 */
	@LogAction("상품 목록 조회")
	List<ProductResponse> getProductsByCategory(Long categoryId) throws CustomException;

	/**
	 * 내가 등록한 상품 조회
	 * @param member 요청한 회원
	 * @return 상품 리스트
	 */
	@LogAction("나의 상품 목록 조회")
	List<ProductResponse> getMyProducts(Member member);

	/**
	 * 상품 상세 조회
	 * @param productId 조회할 상품 식별자
	 * @return 상품 상세 조회정보
	 */
	@LogAction("상품 상세 조회")
	ProductResponse getProductById(Long productId) throws CustomException;
}
