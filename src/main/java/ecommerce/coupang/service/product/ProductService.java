package ecommerce.coupang.service.product;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.request.product.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.UpdateProductVariantRequest;
import ecommerce.coupang.repository.product.ProductVariantRepository;

import ecommerce.coupang.service.category.CategoryService;
import ecommerce.coupang.service.product.option.CategoryOptionService;
import ecommerce.coupang.service.product.option.VariantOptionService;
import ecommerce.coupang.common.utils.StoreUtils;
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
@Transactional(readOnly = true)
@LogLevel("ProductService")
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductVariantRepository productVariantRepository;
	private final StoreQueryService storeQueryService;
	private final CategoryService categoryService;
	private final CategoryOptionService categoryOptionService;
	private final VariantOptionService variantOptionService;

	/**
	 * 상품 등록
	 * @param request 상품 추가 요청 정보
	 * @param member 요청한 회원
	 * @return 생성한 상품 ID
	 */
	@LogAction("상품 등록")
	@Transactional
	public Product createProduct(CreateProductRequest request, Long storeId, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());
		Store store = storeQueryService.findStore(storeId);
		StoreUtils.validateStoreOwner(store, member);

		Product product = Product.create(request, store, category);

		addCategoryOptionsToProduct(request, product);

		for (CreateProductVariantRequest variantRequest : request.getVariants()) {
			ProductVariant productVariant = ProductVariant.create(variantRequest, product);

			addVariantToProduct(productVariant, variantRequest, product);
		}

		productRepository.save(product);
		return product;
	}

	/**
	 * 변형 상품 추가
	 * @param productId 상품 ID
	 * @param request 변형 상품 추가 요청 정보
	 * @param member 요청한 회원
	 * @return 변형 상품
	 */
	@LogAction("변형 상품 추가")
	@Transactional
	public ProductVariant addProductVariant(Long productId, CreateProductVariantRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		StoreUtils.validateStoreOwner(product.getStore(), member);

		ProductVariant productVariant = ProductVariant.create(request, product);

		addVariantToProduct(productVariant, request, product);

		productVariantRepository.save(productVariant);
		return productVariant;
	}

	/**
	 * 상품 수정
	 * @param request 상품 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 상품
	 */
	@LogAction("상품 수정")
	@Transactional
	public Product updateProduct(UpdateProductRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(request.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		StoreUtils.validateStoreOwner(product.getStore(), member);

		product.update(request);

		if (!request.getCategoryOptions().isEmpty()) {
			// TODO 옵션 전체 삭제 후 추가 -> 변경된 옵션만 수정하도록
			product.getProductOptions().clear();

			for (UpdateProductRequest.UpdateCategoryOptionsRequest c : request.getCategoryOptions()) {
				ProductCategoryOption productCategoryOption = categoryOptionService.createProductCategoryOption(c.getOptionValueId(), product);

				product.addProductOptions(productCategoryOption);
			}
		}

		return product;
	}

	/**
	 * 상품 변형 수정
	 * @param request 변형 상품 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 변형 상품
	 */
	@LogAction("상품 변형 수정")
	@Transactional
	public ProductVariant updateProductVariant(UpdateProductVariantRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(request.getId());
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		productVariant.update(request);

		if (!request.getVariantOptions().isEmpty()) {
			// TODO 옵션 전체 삭제 후 추가 -> 변경된 옵션만 수정하도록
			productVariant.getProductVariantOptions().clear();

			for (UpdateProductVariantRequest.UpdateVariantOption o : request.getVariantOptions()) {
				ProductVariantOption productVariantOption = variantOptionService.createProductVariantOption(o.getOptionValueId(), productVariant);

				productVariant.addProductVariantOptions(productVariantOption);
			}
		}

		return productVariant;
	}

	/**
	 * 대표 상품 변경
	 * @param productVariantId 대표상품으로 지정할 상품 변형 ID
	 * @param member 요청한 회원
	 * @return 변경된 대표 상품
	 */
	@LogAction("대표 상품 변경")
	@Transactional
	public ProductVariant updateDefaultProduct(Long productVariantId, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		ProductVariant defaultProductVariant = productVariantRepository.findByProductIdAndDefault(productVariant.getProduct().getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		/* 대표상품과 변경 요청한 상품이같을 시 예외 */
		if (defaultProductVariant.equals(productVariant))
			throw new CustomException(ErrorCode.ALREADY_DEFAULT_PRODUCT);

		productVariant.changeDefault(true);
		defaultProductVariant.changeDefault(false);

		return productVariant;
	}

	/**
	 * 상품 재고 변경
	 * 재고가 0이면 상품 상태가 자동으로 NO_STOCK(재고없음)으로 변경
	 * @param productVariantId 변경할 상품 변형 ID
	 * @param request 재고 변경 요청 정보
	 * @param member 요청한 회원
	 * @return 변경된 상품 변형
	 */
	@LogAction("상품 재고 변경")
	@Transactional
	public ProductVariant updateProductStock(Long productVariantId, UpdateProductStockRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		productVariant.changeStock(request.getStockQuantity());

		return productVariant;
	}

	/**
	 * 상품 상태 변경
	 * 상품 상태가 NO_STOCK(재고없음)이면 자동으로 상품 재고 0으로 변경
	 * @param productVariantId 변경할 상품 변형 ID
	 * @param request 상태 변경 요청 정보
	 * @param member 요청한 회원
	 * @return 변경된 상품 변형
	 */
	@LogAction("상품 상태 변경")
	@Transactional
	public ProductVariant updateProductStatus(Long productVariantId, UpdateProductStatusRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		productVariant.changeStatus(request.getStatus());

		return productVariant;
	}

	/**
	 * 상품 삭제 (Soft)
	 * @param productId 상품 ID
	 * @return 삭제한 상품
	 */
	@LogAction("상품 삭제")
	@Transactional
	public Product deleteProduct(Long productId, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		StoreUtils.validateStoreOwner(product.getStore(), member);

		product.delete();
		return product;
	}

	/**
	 * 변형 상품 삭제 (Soft)
	 * @param productVariantId 변형 상품 ID
	 * @return 삭제한 변형 상품
	 */
	@LogAction("변형 상품 삭제")
	@Transactional
	public ProductVariant deleteProductVariant(Long productVariantId, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		productVariant.delete();
		return productVariant;
	}

	/* 상품 변형 가져오기 */
	private ProductVariant getProductVariantWithMember(Long productVariantId) throws CustomException {
		return productVariantRepository.findByIdWithMember(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	/* ProductVariantOption -> ProductVariant 추가 -> Product 추가 */
	private void addVariantToProduct(ProductVariant productVariant, CreateProductVariantRequest request, Product product) throws CustomException {
		for (CreateProductVariantRequest.VariantOptionRequest o : request.getVariantOptions()) {
			ProductVariantOption productVariantOption = variantOptionService.createProductVariantOption(o.getOptionValueId(), productVariant);

			productVariant.addProductVariantOptions(productVariantOption);
		}

		product.addProductVariants(productVariant);
	}

	/* ProductOption -> Product 추가 */
	private void addCategoryOptionsToProduct(CreateProductRequest request, Product product) throws CustomException {
		for (CreateProductRequest.CategoryOptionsRequest c : request.getCategoryOptions()) {
			ProductCategoryOption productCategoryOption = categoryOptionService.createProductCategoryOption(c.getOptionValueId(), product);

			product.addProductOptions(productCategoryOption);
		}
	}
}

