package ecommerce.coupang.service.product;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.option.CategoryOptionsRequest;

import ecommerce.coupang.service.category.CategoryService;
import ecommerce.coupang.service.product.option.ProductCategoryOptionService;
import ecommerce.coupang.common.utils.store.StoreUtils;
import ecommerce.coupang.service.store.query.StoreQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
@LogLevel("ProductService")
public class ProductService {

	private final ProductRepository productRepository;
	private final StoreQueryService storeQueryService;
	private final CategoryService categoryService;
	private final ProductCategoryOptionService productCategoryOptionService;
	private final ProductVariantFactory productVariantFactory;

	/**
	 * 상품 등록
	 * @param request 상품 추가 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 상품 ID
	 */
	@LogAction("상품 등록")
	public Product createProduct(CreateProductRequest request, Long storeId, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());
		Store store = storeQueryService.findStore(storeId);
		StoreUtils.validateStoreOwner(store, member);

		Product product = Product.create(request, store, category);

		for (CategoryOptionsRequest categoryOption : request.getCategoryOptions())
			addCategoryOptionsToProduct(categoryOption.getOptionValueId(), product);

		for (CreateProductVariantRequest variantRequest : request.getVariants())
			productVariantFactory.createVariantAndOptions(variantRequest, product);

		productRepository.save(product);
		return product;
	}

	/**
	 * 상품 수정
	 * @param request 상품 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 상품
	 */
	@LogAction("상품 수정")
	public Product updateProduct(UpdateProductRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(request.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		StoreUtils.validateStoreOwner(product.getStore(), member);

		product.update(request);

		if (!request.getCategoryOptions().isEmpty()) {
			// TODO 옵션 전체 삭제 후 추가 -> 변경된 옵션만 수정하도록
			product.getProductOptions().clear();

			for (CategoryOptionsRequest categoryOptionsRequest : request.getCategoryOptions())
				addCategoryOptionsToProduct(categoryOptionsRequest.getOptionValueId(), product);
		}

		return product;
	}

	/**
	 * 상품 삭제 (Soft)
	 * @param productId 상품 ID
	 * @return 삭제한 상품
	 */
	@LogAction("상품 삭제")
	public Product deleteProduct(Long productId, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		StoreUtils.validateStoreOwner(product.getStore(), member);

		product.delete();
		return product;
	}

	/* ProductOption -> Product 추가 */
	private void addCategoryOptionsToProduct(Long optionValueId, Product product) throws CustomException {
		ProductCategoryOption productCategoryOption = productCategoryOptionService.createProductCategoryOption(optionValueId, product);

		product.addProductOptions(productCategoryOption);
	}
}

