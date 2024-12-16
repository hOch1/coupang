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
	private final ProductDetailService productDetailService;
	private final CategoryService categoryService;
	private final ProductOptionService productOptionService;
	private final StoreService storeService;
	private final OptionValueService optionValueService;

	@Override
	@Transactional
	public Product createProduct(CreateProductRequest request, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());
		Store store = storeService.findStore(request.getStoreId());

		if (!store.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);

		Product product = Product.create(request, store, category);
		productRepository.save(product);

		for (CreateProductRequest.CreateDetailRequest detailRequest : request.getDetails()) {
			ProductDetail productDetail = productDetailService.createProductDetail(detailRequest, product);

			for (Long optionId : detailRequest.getOptions())
				productOptionService.createProductOption(optionId, productDetail);
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
		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		return productRepository.findByCategoriesAndOptions(categories, optionValues);
	}

	@Override
	public List<Product> findProductsByStore(Long storeId) throws CustomException {
		Store store = storeService.findStore(storeId);

		return productRepository.findByStore(store.getId());
	}

	@Override
	public List<Product> findProductsByStoreAndOptions(Long storeId, List<Long> options) throws CustomException {
		Store store = storeService.findStore(storeId);

		List<OptionValue> optionValues = new ArrayList<>();

		for (Long optionId : options)
			optionValues.add(optionValueService.findOptionValue(optionId));

		return productRepository.findByStoreAndOptions(store.getId(),
			optionValues);
	}

	@Override
	public List<Product> findProductsByStoreAndCategory(Long storeId, Long categoryId) throws CustomException {
		List<Category> categories = categoryService.findAllSubCategories(categoryId);

		Store store = storeService.findStore(storeId);

		return productRepository.findByCategoriesAndStore(store.getId(), categories);
	}

	@Override
	public List<Product> findProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId,
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

