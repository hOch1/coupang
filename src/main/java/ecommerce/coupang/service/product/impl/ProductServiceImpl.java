package ecommerce.coupang.service.product.impl;

import java.util.List;

import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import ecommerce.coupang.repository.category.CategoryOptionValueRepository;
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

	@Override
	@Transactional
	public Product createProduct(CreateProductRequest request, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());
		Store store = storeRepository.findByIdWithMember(request.getStoreId())
				.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		Product product = Product.create(request, store, category);

		for (CreateProductRequest.CategoryOptionsRequest c : request.getCategoryOptions()) {
			CategoryOptionValue categoryOptionValue = categoryOptionValueRepository.findById(c.getOptionValueId())
					.orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

			ProductCategoryOption productCategoryOption = ProductCategoryOption.create(product, categoryOptionValue);
			product.addProductOptions(productCategoryOption);
		}

		for (CreateProductRequest.VariantRequest v : request.getVariants()) {
			ProductVariant productVariant = ProductVariant.create(v, product);

			for (CreateProductRequest.VariantRequest.VariantOption o : v.getVariantOptions()) {
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
	public Product updateProduct(UpdateProductRequest request, Long productId, Member member) throws CustomException {
		// TODO 상품 수정
		return null;
	}

	@Override
	public List<ProductVariant> findProductsByCategory(Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		List<Product> products = productRepository.findByCategories(categories);

		return productVariantRepository.findByProducts(products);
	}

	@Override
	public List<Product> findProductsByCategoryAndOptions(Long categoryId, List<Long> options) throws CustomException {
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
	public List<Product> findProductsByStoreAndOptions(Long storeId, List<Long> options) throws CustomException {
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
	public List<Product> findProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId, List<Long> options) throws CustomException {

		return List.of();
	}

	@Override
	public Product findProduct(Long productId) throws CustomException {
		return productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}
}

