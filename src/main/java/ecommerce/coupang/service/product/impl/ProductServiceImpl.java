package ecommerce.coupang.service.product.impl;

import java.util.List;

import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import ecommerce.coupang.dto.request.product.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.UpdateProductVariantRequest;
import ecommerce.coupang.dto.response.product.ProductDetailResponse;
import ecommerce.coupang.repository.category.CategoryOptionValueRepository;
import ecommerce.coupang.repository.product.ProductCategoryOptionRepository;
import ecommerce.coupang.repository.product.ProductVariantOptionRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.repository.product.VariantOptionValueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductVariantRepository productVariantRepository;
	private final VariantOptionValueRepository variantOptionValueRepository;
	private final CategoryService categoryService;
	private final CategoryOptionValueRepository categoryOptionValueRepository;
	private final StoreRepository storeRepository;
	private final ProductVariantOptionRepository productVariantOptionRepository;
	private final ProductCategoryOptionRepository productCategoryOptionRepository;

	@Override
	@Transactional
	public Product createProduct(CreateProductRequest request, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());
		Store store = storeRepository.findByIdWithMember(request.getStoreId())
				.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		validateMember(store, member);

		Product product = Product.create(request, store, category);

		for (CreateProductRequest.CategoryOptionsRequest c : request.getCategoryOptions()) {
			CategoryOptionValue categoryOptionValue = categoryOptionValueRepository.findById(c.getOptionValueId())
					.orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

			ProductCategoryOption productCategoryOption = ProductCategoryOption.create(product, categoryOptionValue);
			product.addProductOptions(productCategoryOption);
		}

		for (CreateProductRequest.VariantRequest v : request.getVariants()) {
			ProductVariant productVariant = ProductVariant.create(v, product);

			for (CreateProductRequest.VariantRequest.VariantOptionRequest o : v.getVariantOptions()) {
				VariantOptionValue variantOptionValue = variantOptionValueRepository.findById(o.getOptionValueId())
						.orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

				ProductVariantOption productVariantOption = ProductVariantOption.create(productVariant, variantOptionValue);
				productVariant.addProductVariantOption(productVariantOption);
			}

			product.addProductVariant(productVariant);
		}

		productRepository.save(product);

		return product;
	}

	@Override
	@Transactional
	public Product updateProduct(UpdateProductRequest request, Member member) throws CustomException {
		Product product = productRepository.findByIdWithStoreAndCategory(request.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		validateMember(product.getStore(), member);

		product.update(request);

		if (!request.getCategoryOptions().isEmpty()) {
			// TODO 옵션 전체 삭제 후 추가 -> 변경된 옵션만 수정하도록
			product.getProductOptions().clear();

			for (UpdateProductRequest.UpdateCategoryOptionsRequest c : request.getCategoryOptions()) {
				CategoryOptionValue categoryOptionValue = categoryOptionValueRepository.findById(c.getOptionValueId())
					.orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

				ProductCategoryOption productCategoryOption = ProductCategoryOption.create(product, categoryOptionValue);
				product.addProductOptions(productCategoryOption);
			}
		}

		return product;
	}

	@Override
	@Transactional
	public ProductVariant updateProductVariant(UpdateProductVariantRequest request, Member member) throws CustomException {
		ProductVariant productVariant = productVariantRepository.findByIdWithMember(request.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		validateMember(productVariant.getProduct().getStore(), member);

		productVariant.update(request);

		if (!request.getVariantOptions().isEmpty()) {
			// TODO 옵션 전체 삭제 후 추가 -> 변경된 옵션만 수정하도록
			productVariant.getProductVariantOptions().clear();

			for (UpdateProductVariantRequest.UpdateVariantOption o : request.getVariantOptions()) {
				VariantOptionValue variantOptionValue = variantOptionValueRepository.findById(o.getOptionValueId())
					.orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

				ProductVariantOption productVariantOption = ProductVariantOption.create(productVariant, variantOptionValue);
				productVariant.addProductVariantOption(productVariantOption);
			}
		}

		return productVariant;
	}

	@Override
	public List<ProductVariant> findProductsByCategory(Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		List<Product> products = productRepository.findByCategories(categories);

		return productVariantRepository.findByProducts(products);
	}

	@Override
	public List<Product> findProductsByCategoryAndOptions(Long categoryId, List<Long> categoryOptions, List<Long> variantOptions) throws CustomException {

		return List.of();
	}

	@Override
	public List<ProductVariant> findProductsByStore(Long storeId) throws CustomException {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
		List<Product> products = productRepository.findByStore(store.getId());

		return productVariantRepository.findByProducts(products);
	}

	@Override
	public List<Product> findProductsByStoreAndOptions(Long storeId, List<Long> categoryOptions, List<Long> variantOptions) throws CustomException {
		return List.of();
	}

	@Override
	public List<ProductVariant> findProductsByStoreAndCategory(Long storeId, Long categoryId) throws CustomException {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		List<Product> products = productRepository.findByCategoriesAndStore(store.getId(), categories);

		return productVariantRepository.findByProducts(products);
	}

	@Override
	public List<Product> findProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId, List<Long> categoryOptions, List<Long> variantOptions) throws CustomException {

		return List.of();
	}

	@Override
	public ProductDetailResponse findProduct(Long productVariantId) throws CustomException {
		ProductVariant productVariant = productVariantRepository.findByIdWithStoreAndCategory(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		List<ProductCategoryOption> productCategoryOptions = productCategoryOptionRepository.findByProductId(productVariant.getProduct().getId());
		List<ProductVariantOption> productVariantOptions = productVariantOptionRepository.findByProductVariantId(productVariantId);

		return ProductDetailResponse.from(productVariant, productCategoryOptions, productVariantOptions);
	}

	@Override
	@Transactional
	public ProductVariant updateDefaultProduct(Long productVariantId, Member member) throws CustomException {
		ProductVariant productVariant = productVariantRepository.findByIdWithMember(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		validateMember(productVariant.getProduct().getStore(), member);

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
		ProductVariant productVariant = productVariantRepository.findByIdWithMember(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		validateMember(productVariant.getProduct().getStore(), member);

		productVariant.changeStock(request.getStockQuantity());

		return productVariant;
	}

	@Override
	@Transactional
	public ProductVariant updateProductStatus(Long productVariantId, UpdateProductStatusRequest request, Member member) throws CustomException {
		ProductVariant productVariant = productVariantRepository.findByIdWithMember(productVariantId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		validateMember(productVariant.getProduct().getStore(), member);

		productVariant.changeStatus(request.getStatus());

		return productVariant;
	}

	private void validateMember(Store store, Member member) throws CustomException {
		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}
}

