package ecommerce.coupang.service.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.common.utils.StoreUtils;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductVariantRequest;
import ecommerce.coupang.dto.request.product.option.VariantOptionRequest;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.product.option.ProductVariantOptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductVariantService {

	private final ProductRepository productRepository;
	private final ProductVariantRepository productVariantRepository;
	private final ProductVariantOptionService productVariantOptionService;

	/**
	 * 변형 상품 추가
	 * @param productId 상품 ID
	 * @param request 변형 상품 추가 요청 정보
	 * @param member 요청한 회원
	 * @return 변형 상품
	 */
	@LogAction("변형 상품 추가")
	public ProductVariant addProductVariant(Long productId, CreateProductVariantRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		StoreUtils.validateStoreOwner(product.getStore(), member);

		ProductVariant productVariant = ProductVariant.create(request, product);

		for (VariantOptionRequest variantOption : request.getVariantOptions())
			addVariantOptionToProductVariant(productVariant, variantOption.getOptionValueId());

		product.addProductVariants(productVariant);

		productVariantRepository.save(productVariant);
		return productVariant;
	}

	/**
	 * 상품 변형 수정
	 * @param request 변형 상품 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 변형 상품
	 */
	@LogAction("상품 변형 수정")
	public ProductVariant updateProductVariant(Long variantId, UpdateProductVariantRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(variantId);
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		productVariant.update(request);

		if (!request.getVariantOptions().isEmpty()) {
			// TODO 옵션 전체 삭제 후 추가 -> 변경된 옵션만 수정하도록
			productVariant.getProductVariantOptions().clear();

			for (VariantOptionRequest optionRequest : request.getVariantOptions())
				addVariantOptionToProductVariant(productVariant, optionRequest.getOptionValueId());
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
	public ProductVariant updateDefaultProduct(Long productVariantId, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		ProductVariant defaultProductVariant = productVariantRepository.findByProductIdAndDefault(productVariant.getProduct().getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		/* 대표상품과 변경 요청한 상품이 같을시 예외 */
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
	public ProductVariant updateProductStatus(Long productVariantId, UpdateProductStatusRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		StoreUtils.validateStoreOwner(productVariant.getProduct().getStore(), member);

		productVariant.changeStatus(request.getStatus());

		return productVariant;
	}

	/**
	 * 변형 상품 삭제 (Soft)
	 * @param productVariantId 변형 상품 ID
	 * @return 삭제한 변형 상품
	 */
	@LogAction("변형 상품 삭제")
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

	/* ProductVariantOption -> ProductVariant 추가 */
	private void addVariantOptionToProductVariant(ProductVariant productVariant, Long optionValueId) throws CustomException {
		ProductVariantOption productVariantOption = productVariantOptionService.createProductVariantOption(optionValueId, productVariant);

		productVariant.addProductVariantOptions(productVariantOption);
	}
}
