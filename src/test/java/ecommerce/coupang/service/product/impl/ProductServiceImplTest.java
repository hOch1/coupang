package ecommerce.coupang.service.product.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductOptionValue;
import ecommerce.coupang.domain.product.ProductStatus;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionValueService;
import ecommerce.coupang.service.product.ProductOptionService;
import ecommerce.coupang.service.store.StoreService;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryService categoryService;

	@Mock
	private ProductOptionService productOptionService;

	@Mock
	private StoreService storeService;

	@Mock
	private OptionValueService optionValueService;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	@DisplayName("상품 등록 테스트")
	void createProduct() throws CustomException {
		List<Long> options = new ArrayList<>();
		options.add(1L);
		options.add(8L);
		options.add(9L);

		CreateProductRequest request = new CreateProductRequest(
			"product1",
			"first product",
			10000,
			50,
			3L,
			ProductStatus.ACTIVE,
			1L,
			options
		);

		Member mockMember = mock(Member.class);
		Store mockStore = mock(Store.class);
		Category mockCategory = mock(Category.class);
		Product mockProduct = mock(Product.class);
		when(mockProduct.getId()).thenReturn(1L);
		ProductOptionValue mockProductOptionValue = mock(ProductOptionValue.class);
		OptionValue mockOptionValue = mock(OptionValue.class);

		when(storeService.findStore(request.getStoreId())).thenReturn(mockStore);
		when(categoryService.findBottomCategory(request.getCategoryId())).thenReturn(mockCategory);
		when(productOptionService.createProductOptionValue(any(OptionValue.class))).thenReturn(mockProductOptionValue);
		when(productRepository.save(any(Product.class))).thenReturn(mockProduct);
		when(optionValueService.findOptionValue(anyLong())).thenReturn(mockOptionValue);

		Product product = productService.createProduct(request, mockMember);

		assertThat(product.getId()).isEqualTo(mockProduct.getId());

		verify(productRepository).save(any(Product.class));
		verify(storeService).findStore(request.getStoreId());
		verify(categoryService).findBottomCategory(request.getCategoryId());
		verify(productOptionService, times(3)).createProductOptionValue(any(OptionValue.class));
	}

	@Test
	@DisplayName("상품 조회 테스트 - 카테고리")
	void getProductsByCategory() throws CustomException {

	}

	@Test
	@DisplayName("상품 조회 테스트 - 카테고리 + 옵션")
	void getProductsByCategoryAndOptions() throws CustomException {
	}

	@Test
	void getProductsByStore() {
	}

	@Test
	void getProductsByStoreAndOptions() {
	}

	@Test
	void getProductsByStoreAndCategory() {
	}

	@Test
	void getProductsByStoreAndCategoryAndOptions() {
	}

	@Test
	void updateProduct() {
	}
}