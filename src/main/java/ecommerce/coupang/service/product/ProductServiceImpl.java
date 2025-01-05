package ecommerce.coupang.service.product;

import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.dto.request.product.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.UpdateProductVariantRequest;
import ecommerce.coupang.repository.product.ProductVariantRepository;

import ecommerce.coupang.service.product.option.CategoryOptionService;
import ecommerce.coupang.service.product.option.VariantOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.category.CategoryService;
import ecommerce.coupang.service.store.StoreService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductVariantRepository productVariantRepository;
	private final StoreService storeService;
	private final CategoryService categoryService;
	private final CategoryOptionService categoryOptionService;
	private final VariantOptionService variantOptionService;

	@Override
	@Transactional
	public Product createProduct(CreateProductRequest request, Long storeId, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());
		Store store = storeService.validateStoreMember(storeId, member);

		Product product = Product.create(request, store, category);

		addCategoryOptionsToProduct(request, product);

		for (CreateProductVariantRequest variantRequest : request.getVariants()) {
			ProductVariant productVariant = ProductVariant.create(variantRequest, product);

			addVariantToProduct(productVariant, variantRequest, product);
		}

		productRepository.save(product);
		return product;
	}

	@Override
	@Transactional
	public ProductVariant addProductVariant(Long productId, CreateProductVariantRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		ProductVariant productVariant = ProductVariant.create(request, product);

		addVariantToProduct(productVariant, request, product);

		productVariantRepository.save(productVariant);
		return productVariant;
	}

	@Override
	@Transactional
	public Product updateProduct(UpdateProductRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(request.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		storeService.validateStoreMember(product.getStore().getId(), member);

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

	@Override
	@Transactional
	public ProductVariant updateProductVariant(UpdateProductVariantRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(request.getId());
		storeService.validateStoreMember(productVariant.getProduct().getStore().getId(), member);

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

	@Override
	@Transactional
	public ProductVariant updateDefaultProduct(Long productVariantId, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		storeService.validateStoreMember(productVariant.getProduct().getStore().getId(), member);

		ProductVariant defaultProductVariant = productVariantRepository.findByProductIdAndDefault(productVariant.getProduct().getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		if (defaultProductVariant.equals(productVariant))
			throw new CustomException(ErrorCode.ALREADY_DEFAULT_PRODUCT);

		productVariant.changeDefault(true);
		defaultProductVariant.changeDefault(false);

		return productVariant;
	}

	@Override
	@Transactional
	public ProductVariant updateProductStock(Long productVariantId, UpdateProductStockRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		storeService.validateStoreMember(productVariant.getProduct().getStore().getId(), member);

		productVariant.changeStock(request.getStockQuantity());

		return productVariant;
	}

	@Override
	@Transactional
	public ProductVariant updateProductStatus(Long productVariantId, UpdateProductStatusRequest request, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		storeService.validateStoreMember(productVariant.getProduct().getStore().getId(), member);

		productVariant.changeStatus(request.getStatus());

		return productVariant;
	}

	@Override
	@Transactional
	public Product deleteProduct(Long productId, Member member) throws CustomException {
		Product product = productRepository.findByIdWithMemberAndCategory(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		storeService.validateStoreMember(product.getStore().getId(), member);

		product.delete();
		return product;
	}

	@Override
	@Transactional
	public ProductVariant deleteProductVariant(Long productVariantId, Member member) throws CustomException {
		ProductVariant productVariant = getProductVariantWithMember(productVariantId);
		storeService.validateStoreMember(productVariant.getProduct().getStore().getId(), member);

		productVariant.delete();
		return productVariant;
	}

	private ProductVariant getProductVariantWithMember(Long productVariantId) throws CustomException {
		return productVariantRepository.findByIdWithMember(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private void addVariantToProduct(ProductVariant productVariant, CreateProductVariantRequest request, Product product) throws CustomException {
		for (CreateProductVariantRequest.VariantOptionRequest o : request.getVariantOptions()) {
			ProductVariantOption productVariantOption = variantOptionService.createProductVariantOption(o.getOptionValueId(), productVariant);

			productVariant.addProductVariantOptions(productVariantOption);
		}

		product.addProductVariants(productVariant);
	}

	private void addCategoryOptionsToProduct(CreateProductRequest request, Product product) throws CustomException {
		for (CreateProductRequest.CategoryOptionsRequest c : request.getCategoryOptions()) {
			ProductCategoryOption productCategoryOption = categoryOptionService.createProductCategoryOption(c.getOptionValueId(), product);

			product.addProductOptions(productCategoryOption);
		}
	}
}

