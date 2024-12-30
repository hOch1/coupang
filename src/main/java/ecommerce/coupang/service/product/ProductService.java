package ecommerce.coupang.service.product;

import java.util.List;

import ecommerce.coupang.aop.log.LogAction;
import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.ProductSort;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.UpdateProductVariantRequest;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
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
	Product createProduct(CreateProductRequest request, Long storeId, Member member) throws CustomException;

	/**
	 * 상품 수정
	 * @param request 상품 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 상품
	 */
	@LogAction("상품 수정")
	Product updateProduct(UpdateProductRequest request, Member member) throws CustomException;

	/**
	 * 상품 변형 수정
	 * @param request 변형 상품 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 변형 상품
	 */
	@LogAction("상품 변형 수정")
	ProductVariant updateProductVariant(UpdateProductVariantRequest request, Member member) throws CustomException;

	/**
	 * 상품 상세 조회
	 * @param productVariantId 조회할 상품 ID
	 * @return 상품 상세 조회정보
	 */
	@LogAction("상품 상세 조회")
	ProductDetailResponse findProduct(Long productVariantId) throws CustomException;

	/**
	 * 대표 상품 변경
	 * @param productVariantId 대표상품으로 지정할 상품 변형 ID
	 * @param member 요청한 회원
	 * @return 변경된 대표 상품
	 */
	@LogAction("대표 상품 변경")
	ProductVariant updateDefaultProduct(Long productVariantId, Member member) throws CustomException;

	/**
	 * 상품 재고 변경
	 * 재고가 0이면 상품 상태가 자동으로 NO_STOCK(재고없음)으로 변경
	 * @param productVariantId 변경할 상품 변형 ID
	 * @param request 재고 변경 요청 정보
	 * @param member 요청한 회원
	 * @return 변경된 상품 변형
	 */
	@LogAction("상품 재고 변경")
	ProductVariant updateProductStock(Long productVariantId, UpdateProductStockRequest request, Member member) throws CustomException;

	/**
	 * 상품 상태 변경
	 * 상품 상태가 NO_STOCK(재고없음)이면 자동으로 상품 재고 0으로 변경
	 * @param productVariantId 변경할 상품 변형 ID
	 * @param request 상태 변경 요청 정보
	 * @param member 요청한 회원
	 * @return 변경된 상품 변형
	 */
	@LogAction("상품 상태 변경")
	ProductVariant updateProductStatus(Long productVariantId, UpdateProductStatusRequest request, Member member) throws CustomException;

	/**
	 * 상품 삭제 (Soft)
	 * @param productId 상품 ID
	 * @return 삭제한 상품
	 */
	@LogAction("상품 삭제")
	Product deleteProduct(Long productId, Member member) throws CustomException;

	/**
	 * 변형 상품 삭제 (Soft)
	 * @param productVariantId 변형 상품 ID
	 * @return 삭제한 변형 상품
	 */
	@LogAction("변형 상품 삭제")
	ProductVariant deleteProductVariant(Long productVariantId, Member member) throws CustomException;

	/**
	 * 옵션별 상품 조회
	 * @param categoryId 카테고리 ID
	 * @param storeId 상점 ID
	 * @param categoryOptions 카테고리 옵션 값 ID
	 * @param variantOptions 변형 옵션 값 ID
	 * @return 조회 상품 리스트
	 * @throws CustomException
	 */
	@LogAction("상품 목록 조회")
	List<ProductResponse> search(Long categoryId, Long storeId, List<Long> categoryOptions, List<Long> variantOptions, ProductSort sort) throws CustomException;
}
