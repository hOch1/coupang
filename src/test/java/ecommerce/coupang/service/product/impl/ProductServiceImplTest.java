package ecommerce.coupang.service.product.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.domain.product.ProductStatus;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.OptionValueService;
import ecommerce.coupang.service.product.ProductDetailService;
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

	@Mock
	private ProductDetailService productDetailService;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	@DisplayName("상품 등록 테스트")
	void createProduct() throws CustomException {
		// Arrange
		Long categoryId = 3L;
		Long storeId = 1L;
		CreateProductRequest request = request();

		// 준비된 mock 데이터
		Member mockMember = mock(Member.class);
		Category mockCategory = mock(Category.class);
		Store mockStore = mock(Store.class);
		Product mockProduct = mock(Product.class);
		ProductDetail mockProductDetail = mock(ProductDetail.class);
		OptionValue mockOptionValue = mock(OptionValue.class);

		when(categoryService.findBottomCategory(categoryId)).thenReturn(mockCategory);
		when(storeService.findStore(storeId)).thenReturn(mockStore);
		when(productRepository.save(any(Product.class))).thenReturn(mockProduct);
		when(productDetailService.save(any(CreateProductRequest.CreateDetailRequest.class), eq(mockProduct)))
			.thenReturn(mockProductDetail);
		when(optionValueService.findOptionValue(anyLong())).thenReturn(mockOptionValue);

		// Act
		Product result = productService.createProduct(request, mockMember);

		// Assert
		verify(categoryService).findBottomCategory(categoryId);
		verify(storeService).findStore(storeId);
		verify(productRepository).save(any(Product.class));  // 상품이 저장되어야 함
		verify(productDetailService, times(request.getDetails().size()))
			.save(any(CreateProductRequest.CreateDetailRequest.class), eq(mockProduct));
		verify(optionValueService, times(request.getDetails().get(0).getOptions().size()))
			.findOptionValue(any());
		verify(productOptionService, times(request.getDetails().get(0).getOptions().size()))
			.save(any(OptionValue.class), eq(mockProductDetail));  // 옵션이 저장되어야 함

		assertNotNull(result);
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

	private CreateProductRequest request() {
		CreateProductRequest.CreateDetailRequest detailRequest = new CreateProductRequest.CreateDetailRequest(
			10000,
			100,
			ProductStatus.ACTIVE,
			List.of(1L, 6L, 9L, 10L, 11L, 12L)
		);

		return new CreateProductRequest(
			"ProductA",
			"Test Product",
			3L,
			1L,
			List.of(detailRequest)
		);
	}
}