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
import ecommerce.coupang.repository.product.OptionValueRepository;
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
	private final CategoryService categoryService;
	private final OptionValueRepository optionValueRepository;
	private final StoreRepository storeRepository;

	@Override
	@Transactional
	public Long createProduct(CreateProductRequest request, Member member) throws CustomException {
		Category category = categoryService.findBottomCategory(request.getCategoryId());

		Store store = storeRepository.findById(request.getStoreId()).orElseThrow(() ->
			new CustomException(ErrorCode.STORE_NOT_FOUND));

		Map<Long, Long> options = request.getOptions();
		List<ProductOptionValue> productOptionValues = new ArrayList<>();

		for(Long optionId : options.values()) {
			OptionValue optionValue = optionValueRepository.findById(optionId).orElseThrow(() ->
				new CustomException(ErrorCode.OPTION_VALUE_NOT_FOUND));

			productOptionValues.add(ProductOptionValue.create(optionValue));
		}

		Product saveProduct = productRepository.save(Product.create(request, productOptionValues, store, category));

		return saveProduct.getId();
	}

	@Override
	public List<ProductResponse> getProductsByCategory(Long categoryId) throws CustomException {
		Category category = categoryService.findCategoryById(categoryId);
		List<Category> categories = new ArrayList<>();

		addAllSubCategory(category, categories);

		List<Product> products = productRepository.findByProductInCategory(categories);

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

		return product.getId();
	}

	@Override
	public List<ProductResponse> getMyProducts(Member member) {
		List<Product> products = productRepository.findByStoreId(member.getId());

		return null;
	}

	@Override
	public ProductResponse getProductById(Long productId) throws CustomException {
		Product product = productRepository.findById(productId).orElseThrow(() ->
			new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

		return null;
	}

	private void addAllSubCategory(Category category, List<Category> categories) {
		categories.add(category);
		for (Category children : category.getChildren())
			addAllSubCategory(children, categories);
	}
}

