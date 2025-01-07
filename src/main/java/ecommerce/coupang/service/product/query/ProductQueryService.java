package ecommerce.coupang.service.product.query;

import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.dto.request.PagingRequest;
import ecommerce.coupang.dto.request.product.ProductSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.member.MemberGrade;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.dto.request.product.ProductSort;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductCategoryOptionRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.repository.store.CouponProductRepository;
import ecommerce.coupang.service.category.CategoryService;
import ecommerce.coupang.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

	private final ProductRepository productRepository;
	private final ProductVariantRepository productVariantRepository;
	private final ProductCategoryOptionRepository productCategoryOptionRepository;
	private final ProductVariantOptionRepository productVariantOptionRepository;
	private final CouponProductRepository couponProductRepository;
	private final CategoryService categoryService;
	private final DiscountService discountService;

	/**
	 * 상품 상세 조회
	 * @param productVariantId 조회할 상품 ID
	 * @return 상품 상세 조회정보
	 */
	@LogAction("상품 상세 조회")
	public ProductDetailResponse findProduct(Long productVariantId, MemberGrade memberGrade) throws CustomException {
		ProductVariant productVariant = productVariantRepository.findByIdWithStoreAndCategory(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		List<ProductCategoryOption> productCategoryOptions = productCategoryOptionRepository.findByProductId(productVariant.getProduct().getId());
		List<ProductVariantOption> productVariantOptions = productVariantOptionRepository.findByProductVariantId(productVariant.getId());
		List<CouponProduct> couponProducts = couponProductRepository.findByProductId(productVariant.getProduct().getId());
		Category category = categoryService.findCategoryWithRoot(productVariant.getProduct().getCategory().getId());

		int discountPrice = discountService.calculateMemberDiscount(memberGrade, productVariant.getPrice());

		return ProductDetailResponse.from(productVariant, discountPrice, category, productCategoryOptions, productVariantOptions, couponProducts);
	}

	/**
	 * 옵션별 상품 조회
	 * @param searchRequest 검색 조건
	 * @param sort 정렬 타입
	 * @param pagingRequest 페이징 정보
	 * @return 조회 상품 리스트
     */
	@LogAction("상품 목록 조회")
	public Page<ProductResponse> search(ProductSearchRequest searchRequest, ProductSort sort, PagingRequest pagingRequest, MemberGrade memberGrade) {
		List<Category> categories = new ArrayList<>();

		if (searchRequest.getCategoryId() != null)
			categories = categoryService.findAllSubCategories(searchRequest.getCategoryId());

		Page<ProductResponse> responses = productRepository.searchProducts(searchRequest, categories, sort, PageRequest.of(pagingRequest.getPage(), pagingRequest.getPageSize()));

		responses.forEach(productResponse ->
				productResponse.setMemberDiscountPrice(discountService.calculateMemberDiscount(memberGrade, productResponse.getPrice())));
		return responses;
	}
}
