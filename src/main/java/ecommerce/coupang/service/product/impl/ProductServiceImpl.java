package ecommerce.coupang.service.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionValueService;
import ecommerce.coupang.service.product.ProductDetailService;
import ecommerce.coupang.service.product.ProductOptionService;
import ecommerce.coupang.service.product.ProductService;
import ecommerce.coupang.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryService categoryService;
	private final ProductOptionService productOptionService;
	private final StoreService storeService;
	private final OptionValueService optionValueService;
	private final ProductDetailService productDetailService;

	@Override
	@Transactional
	public Product createProduct(CreateProductRequest request, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());
		Store store = storeService.findStore(request.getStoreId());

		Product product = productRepository.save(Product.create(request, store, category));

		for (CreateProductRequest.CreateDetailRequest detail : request.getDetails()) {
			ProductDetail productDetail = productDetailService.save(detail, product);

			for (Long option : detail.getOptions()) {
				OptionValue optionValue = optionValueService.findOptionValue(option);
				productOptionService.save(optionValue, productDetail);
			}
		}

		return product;
	}

	@Override
	public List<Product> getProductsByCategory(Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		return productRepository.findByCategories(categories);
	}

	@Override
	public List<Product> getProductsByCategoryAndOptions(Long categoryId, List<Long> options) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		return productRepository.findByCategoriesAndOptions(categories, optionValues);
	}

	@Override
	public List<Product> getProductsByStore(Long storeId) throws CustomException {
		Store store = storeService.findStore(storeId);

		return productRepository.findByStore(store.getId());
	}

	@Override
	public List<Product> getProductsByStoreAndOptions(Long storeId, List<Long> options) throws CustomException {
		Store store = storeService.findStore(storeId);

		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		return productRepository.findByStoreAndOptions(store.getId(),
			optionValues);
	}

	@Override
	public List<Product> getProductsByStoreAndCategory(Long storeId, Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);

		Store store = storeService.findStore(storeId);

		return productRepository.findByCategoriesAndStore(store.getId(), categories);
	}

	@Override
	public List<Product> getProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId,
		List<Long> options) throws CustomException {

		List<Category> categories = categoryService.findAllSubCategories(categoryId);
		Store store = storeService.findStore(storeId);
		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		return productRepository.findByStoreAndCategoryAndOptions(store.getId(),
			categories, optionValues);
	}

	@Override
	@Transactional
	public Product updateProduct(UpdateProductRequest request, Long productId, Member member) throws CustomException {
		Product product = getProductById(productId);

		validateProductUpdatePermission(product, member);

		product.update(request);
		return product;
	}

	@Override
	public Product getProductById(Long productId) throws CustomException {
		return productRepository.findById(productId)
			.orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private void validateProductUpdatePermission(Product product, Member member) throws CustomException {
		Store store = product.getStore();

		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}
}

