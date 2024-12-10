package ecommerce.coupang.service.product;

import java.util.List;
import java.util.Map;

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
	 * @param categoryId 카테고리 ID
	 * @return 상품 리스트
	 */
	@LogAction("상품 목록 조회 - 카테고리")
	List<ProductResponse> getProductsByCategory(Long categoryId) throws CustomException;

	/**
	 * 카테고리 + 옵션별 상품 조회 (하위 카테고리 포함)
	 * @param categoryId 카테고리 ID
	 * @param options 옵션 파라미터
	 * @return 상품 리스트
	 */
	@LogAction("상품 목록 조회 카테고리 + 옵션")
	List<ProductResponse> getProductsByCategoryAndOptions(Long categoryId, List<Long> options) throws
		CustomException;

	/**
	 * 상점 별 상품 조회
	 * @param storeId 상점 ID
	 * @return 상품 리스트
	 */
	@LogAction("상품 목록 조회 - 상점")
	List<ProductResponse> getProductsByStore(Long storeId) throws CustomException;

	/**
	 * 상점 + 옵션별 상품 조회
	 * @param storeId 상점 ID
	 * @param options 옵션 파라미터
	 * @return 상품 리스트
	 */
	@LogAction("상품 목록 조회 - 상점 + 옵션")
	List<ProductResponse> getProductsByStoreAndOptions(Long storeId, List<Long> options) throws CustomException;

	/**
	 * 상점 + 카테고리 별 상품 조회 (하위 카테고리 포함)
	 * @param storeId 상점 ID
	 * @param categoryId 카테고리 ID
	 * @return 상품 리스트
	 */
	@LogAction("상품 목록 조회 - 상점 + 카테고리")
	List<ProductResponse> getProductsByStoreAndCategory(Long storeId, Long categoryId) throws CustomException;

	/**
	 * 상점 + 카테고리 + 옵션별 상품 조회 (하위 카테고리 포함)
	 * @param storeId 상점 ID
	 * @param categoryId 카테고리 ID
	 * @param options 옵션 파라미터
	 * @return 상품 리스트
	 */
	@LogAction("상품 목록 조회 - 상점 + 카테고리 + 옵션")
	List<ProductResponse> getProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId, List<Long> options) throws
		CustomException;

	/**
	 * 상품 상세 조회
	 * @param productId 조회할 상품 식별자
	 * @return 상품 상세 조회정보
	 */
	@LogAction("상품 상세 조회")
	ProductResponse getProductById(Long productId) throws CustomException;
}
