package ecommerce.coupang.service.product.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductOptionValue;
import ecommerce.coupang.domain.product.ProductStatus;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionValueService;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryService categoryService;

	@Mock
	private OptionValueService optionValueService;

	@Mock
	private StoreRepository storeRepository;

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
		ProductOptionValue mockOptionValue1 = mock(ProductOptionValue.class);
		ProductOptionValue mockOptionValue2 = mock(ProductOptionValue.class);
		ProductOptionValue mockOptionValue3 = mock(ProductOptionValue.class);

		when(storeRepository.findById(request.getStoreId())).thenReturn(Optional.of(mockStore));
		when(categoryService.findBottomCategory(request.getCategoryId())).thenReturn(mockCategory);
		when(optionValueService.createProductOptionValue(1L)).thenReturn(mockOptionValue1);
		when(optionValueService.createProductOptionValue(8L)).thenReturn(mockOptionValue2);
		when(optionValueService.createProductOptionValue(9L)).thenReturn(mockOptionValue3);
		when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

		Long saveProductId = productService.createProduct(request, mockMember);

		assertThat(saveProductId).isEqualTo(mockProduct.getId());

		verify(productRepository).save(any(Product.class));
		verify(storeRepository).findById(request.getStoreId());
		verify(categoryService).findBottomCategory(request.getCategoryId());
		verify(optionValueService, times(3)).createProductOptionValue(anyLong());
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