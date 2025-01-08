package ecommerce.coupang.service.product;

import ecommerce.coupang.common.aop.log.LogAction;
import ecommerce.coupang.common.aop.log.LogLevel;

import ecommerce.coupang.dto.request.product.option.OptionRequest;
import ecommerce.coupang.service.category.CategoryService;
import ecommerce.coupang.service.product.option.ProductCategoryOptionService;
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
	private final ProductCreateManagement productCreateManagement;
	private final ProductCategoryOptionService productCategoryOptionService;

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
		store.validateOwner(member);

		Product product = productCreateManagement.createProductAndVariantAndOptions(request, store, category);

		productRepository.save(product);
		return product;
	}

	/**
	 * 상품 수정
	 * @param productId 상품 ID
	 * @param request 상품 수정 요청 정보
	 * @param member 요청한 회원
	 * @return 수정한 상품
	 */
	@LogAction("상품 수정")
	public Product updateProduct(Long productId, UpdateProductRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		product.getStore().validateOwner(member);

		product.update(request);

		for (OptionRequest categoryOption : request.getCategoryOptions())
			productCategoryOptionService.update(categoryOption, product);

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
		product.getStore().validateOwner(member);

		product.delete();
		return product;
	}
}

