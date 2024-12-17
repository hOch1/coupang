package ecommerce.coupang.service.product.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.domain.product.ProductOption;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.OptionValueRepository;
import ecommerce.coupang.repository.product.ProductDetailRepository;
import ecommerce.coupang.repository.product.ProductOptionRepository;
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
	private final ProductDetailRepository productDetailRepository;
	private final CategoryService categoryService;
	private final ProductOptionRepository productOptionRepository;
	private final OptionValueRepository optionValueRepository;
	private final StoreRepository storeRepository;

	@Override
	@Transactional
	public Product createProduct(CreateProductRequest request, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());

		Store store = storeRepository.findById(request.getStoreId())
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		Product product = Product.create(request, store, category);
		productRepository.save(product);

		for (CreateProductRequest.CreateDetailRequest detailRequest : request.getDetails()) {
			ProductDetail productDetail = ProductDetail.create(detailRequest, product);
			productDetailRepository.save(productDetail);

			for (Long optionId : detailRequest.getOptions()) {
				OptionValue optionValue = optionValueRepository.findById(optionId)
					.orElseThrow(() -> new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

				ProductOption productOption = ProductOption.create(optionValue, productDetail);
				productOptionRepository.save(productOption);
			}
		}

		return product;
	}

	@Override
	public List<Product> findProductsByCategory(Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		return productRepository.findByCategories(categories);
	}

	@Override
	public List<Product> findProductsByCategoryAndOptions(Long categoryId, List<Long> options) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);

		return productRepository.findByCategoriesAndOptions(categories, options);
	}

	@Override
	public List<Product> findProductsByStore(Long storeId) throws CustomException {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		return productRepository.findByStore(store.getId());
	}

	@Override
	public List<Product> findProductsByStoreAndOptions(Long storeId, List<Long> options) throws CustomException {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		return productRepository.findByStoreAndOptions(store.getId(), options);
	}

	@Override
	public List<Product> findProductsByStoreAndCategory(Long storeId, Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);

		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		return productRepository.findByCategoriesAndStore(store.getId(), categories);
	}

	@Override
	public List<Product> findProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId,
		List<Long> options) throws CustomException {

		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		return productRepository.findByStoreAndCategoryAndOptions(store.getId(), categories, options);
	}

	@Override
	@Transactional
	public Product updateProduct(UpdateProductRequest request, Long productId, Member member) throws CustomException {
		Product product = findProduct(productId);

		validateProductUpdatePermission(product, member);

		product.update(request);
		return product;
	}

	@Override
	public Product findProduct(Long productId) throws CustomException {
		return productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private void validateProductUpdatePermission(Product product, Member member) throws CustomException {
		Store store = product.getStore();

		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}
}

