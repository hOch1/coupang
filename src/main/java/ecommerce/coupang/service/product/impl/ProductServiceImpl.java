package ecommerce.coupang.service.product.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductOptionValue;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.dto.response.product.ProductResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionValueService;
import ecommerce.coupang.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryService categoryService;
	private final OptionValueService optionValueService;
	private final StoreRepository storeRepository;

	@Override
	@Transactional
	public Long createProduct(CreateProductRequest request, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());

		Store store = storeRepository.findById(request.getStoreId()).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		List<Long> options = request.getOptions();
		List<ProductOptionValue> productOptionValues = new ArrayList<>();

		for (Long optionId : options)
			productOptionValues.add(optionValueService.createProductOptionValue(optionId));

		Product saveProduct = productRepository.save(Product.create(request, productOptionValues, store, category));

		return saveProduct.getId();
	}

	@Override
	public List<ProductResponse> getProductsByCategory(Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		List<Product> products = productRepository.findByCategories(categories);

		return products.stream()
			.map(ProductResponse::from)
			.toList();
	}

	@Override
	public List<ProductResponse> getProductsByCategoryAndOptions(Long categoryId, List<Long> options) throws
		CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		List<Product> products = productRepository.findByCategoriesAndOptions(categories, optionValues);

		return products.stream()
			.map(ProductResponse::from)
			.toList();
	}

	@Override
	public List<ProductResponse> getProductsByStore(Long storeId) throws CustomException {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		List<Product> products = productRepository.findByStore(store.getId());

		return products.stream()
			.map(ProductResponse::from)
			.toList();
	}

	@Override
	public List<ProductResponse> getProductsByStoreAndOptions(Long storeId, List<Long> options) throws CustomException {
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		List<Product> products = productRepository.findByStoreAndOptions(store.getId(),
			optionValues);

		return products.stream()
			.map(ProductResponse::from)
			.toList();
	}

	@Override
	public List<ProductResponse> getProductsByStoreAndCategory(Long storeId, Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);

		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		List<Product> products = productRepository.findByCategoriesAndStore(store.getId(), categories);

		return products.stream()
			.map(ProductResponse::from)
			.toList();
	}

	@Override
	public List<ProductResponse> getProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId,
		List<Long> options) throws CustomException {

		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		Store store = storeRepository.findById(storeId).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));
		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		List<Product> products = productRepository.findByStoreAndCategoryAndOptions(store.getId(),
			categories, optionValues);

		return products.stream()
			.map(ProductResponse::from)
			.toList();
	}

	@Override
	@Transactional
	public Long updateProduct(UpdateProductRequest request, Long productId, Member member) throws CustomException {
		Product product = productRepository.findById(productId).orElseThrow(() ->
			new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		product.update(request);
		// TODO
		return product.getId();
	}

	@Override
	public ProductResponse getProductById(Long productId) throws CustomException {
		Product product = productRepository.findById(productId).orElseThrow(() ->
			new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
		// TODO
		return null;
	}

}

