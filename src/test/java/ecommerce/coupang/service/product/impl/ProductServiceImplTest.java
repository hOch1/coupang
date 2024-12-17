package ecommerce.coupang.service.product.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

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
import ecommerce.coupang.domain.product.OptionValue;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.domain.product.ProductOption;
import ecommerce.coupang.domain.product.ProductStatus;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.OptionValueRepository;
import ecommerce.coupang.repository.product.ProductDetailRepository;
import ecommerce.coupang.repository.product.ProductOptionRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryService categoryService;

	@Mock
	private ProductOptionRepository productOptionRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private OptionValueRepository optionValueRepository;

	@Mock
	private ProductDetailRepository productDetailRepository;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	@DisplayName("상품 등록 테스트")
	void createProduct() throws CustomException {
		Long categoryId = 3L;
		Long storeId = 1L;
		CreateProductRequest request = request();

		Member mockMember = mock(Member.class);
		Category mockCategory = mock(Category.class);
		Store mockStore = mock(Store.class);
		when(mockStore.getMember()).thenReturn(mockMember);
		Product mockProduct = mock(Product.class);
		ProductDetail mockProductDetail = mock(ProductDetail.class);
		OptionValue mockOptionValue = mock(OptionValue.class);

		when(categoryService.findBottomCategory(categoryId)).thenReturn(mockCategory);
		when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));
		when(productRepository.save(any(Product.class))).thenReturn(mockProduct);
		when(productDetailRepository.save(any(ProductDetail.class))).thenReturn(mockProductDetail);
		when(optionValueRepository.findById(anyLong())).thenReturn(Optional.of(mockOptionValue));

		Product saveProduct = productService.createProduct(request, mockMember);

		assertNotNull(saveProduct);
		assertThat(saveProduct.getProductDetails()).contains(mockProductDetail);
		assertThat(saveProduct.getCategory()).isEqualTo(mockCategory);

		verify(productRepository).save(any(Product.class));
		verify(productDetailRepository, times(1)).save(any(ProductDetail.class));
		verify(productOptionRepository, times(6)).save(any(ProductOption.class));
	}

	@Test
	@DisplayName("상품 등록 테스트 - (실패) 상점등록 회원과 요청 회원 틀림")
	void createProductNotSameStoreMember() throws CustomException {
		Long categoryId = 3L;
		Long storeId = 1L;
		CreateProductRequest request = request();

		Member mockMember = mock(Member.class);
		Member storeMember = mock(Member.class);
		Category mockCategory = mock(Category.class);
		Store mockStore = mock(Store.class);
		when(mockStore.getMember()).thenReturn(storeMember);

		when(categoryService.findBottomCategory(categoryId)).thenReturn(mockCategory);
		when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.createProduct(request, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);

		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	@DisplayName("상품 등록 테스트 - (실패) 최하위 카테고리가 아님")
	void createProductNotBottomCategory() throws CustomException {
		Long categoryId = 3L;
		CreateProductRequest request = request();

		Member mockMember = mock(Member.class);

		given(categoryService.findBottomCategory(categoryId))
			.willThrow(new CustomException(ErrorCode.IS_NOT_BOTTOM_CATEGORY));

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.createProduct(request, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.IS_NOT_BOTTOM_CATEGORY);

		verify(productRepository, never()).save(any(Product.class));
	}

	@Test
	@DisplayName("싱품 등록 테스트 - (실패) 옵션을 찾을 수 없음")
	void createProductNotFoundOption() throws CustomException {
		Long categoryId = 3L;
		Long storeId = 1L;
		CreateProductRequest request = request();

		Member mockMember = mock(Member.class);
		Category mockCategory = mock(Category.class);
		Store mockStore = mock(Store.class);
		given(mockStore.getMember()).willReturn(mockMember);
		Product mockProduct = mock(Product.class);
		ProductDetail mockProductDetail = mock(ProductDetail.class);

		when(categoryService.findBottomCategory(categoryId)).thenReturn(mockCategory);
		when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));
		when(productRepository.save(any(Product.class))).thenReturn(mockProduct);
		when(productDetailRepository.save(any(ProductDetail.class))).thenReturn(mockProductDetail);
		when(optionValueRepository.findById(anyLong())).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.createProduct(request, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.OPTION_VALUE_NOT_FOUND);

		verify(productRepository).save(any(Product.class));
		verify(productDetailRepository, times(1)).save(any(ProductDetail.class));
		verify(productOptionRepository, never()).save(any(ProductOption.class));
	}

	@Test
	@DisplayName("상품 조회 테스트 - 카테고리")
	void getProductsByCategory() throws CustomException {
		Long categoryId = 1L;

		Category mockCategory = mock(Category.class);
		List<Category> mockCategories = List.of(mockCategory);
		given(categoryService.findAllSubCategories(categoryId)).willReturn(mockCategories);

		Product mockProduct = mock(Product.class);
		List<Product> mockProducts = List.of(mockProduct);
		given(productRepository.findByCategories(mockCategories)).willReturn(mockProducts);

		List<Product> products = productService.findProductsByCategory(categoryId);

		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(mockProduct, products.get(0));
	}

	@Test
	@DisplayName("상품 조회 테스트 - 카테고리 + 옵션")
	void getProductsByCategoryAndOptions() throws CustomException {
		Long categoryId = 1L;
		List<Long> options = List.of(1L);

		Category mockCategory = mock(Category.class);
		List<Category> mockCategories = List.of(mockCategory);
		given(categoryService.findAllSubCategories(categoryId)).willReturn(mockCategories);

		Product mockProduct = mock(Product.class);
		List<Product> mockProducts = List.of(mockProduct);
		given(productRepository.findByCategoriesAndOptions(mockCategories, options)).willReturn(mockProducts);

		List<Product> products = productService.findProductsByCategoryAndOptions(categoryId, options);

		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(mockProduct, products.get(0));
	}

	@Test
	@DisplayName("상품 조회 테스트 - 상점")
	void getProductsByStore() throws CustomException {
		Long storeId = 1L;

		Store mockStore = mock(Store.class);
		given(mockStore.getId()).willReturn(storeId);
		given(storeRepository.findById(storeId)).willReturn(Optional.of(mockStore));

		Product mockProduct = mock(Product.class);
		List<Product> mockProducts = List.of(mockProduct);
		given(productRepository.findByStore(storeId)).willReturn(mockProducts);

		List<Product> products = productService.findProductsByStore(storeId);

		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(mockProduct, products.get(0));
	}

	@Test
	@DisplayName("상품 조회 테스트 - 상점 + 옵션")
	void getProductsByStoreAndOptions() throws CustomException {
		Long storeId = 1L;
		List<Long> options = List.of(1L);

		Store mockStore = mock(Store.class);
		given(mockStore.getId()).willReturn(storeId);
		given(storeRepository.findById(storeId)).willReturn(Optional.of(mockStore));

		Product mockProduct = mock(Product.class);
		List<Product> mockProducts = List.of(mockProduct);
		given(productRepository.findByStoreAndOptions(storeId, options)).willReturn(mockProducts);

		List<Product> products = productService.findProductsByStoreAndOptions(storeId, options);

		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(mockProduct, products.get(0));
	}

	@Test
	@DisplayName("상품 조회 테스트 - 상점 + 카테고리")
	void getProductsByStoreAndCategory() throws CustomException {
		Long storeId = 1L;
		Long categoryId = 1L;

		Category mockCategory = mock(Category.class);
		List<Category> mockCategories = List.of(mockCategory);
		given(categoryService.findAllSubCategories(categoryId)).willReturn(mockCategories);

		Store mockStore = mock(Store.class);
		given(mockStore.getId()).willReturn(storeId);
		given(storeRepository.findById(storeId)).willReturn(Optional.of(mockStore));

		Product mockProduct = mock(Product.class);
		List<Product> mockProducts = List.of(mockProduct);
		given(productRepository.findByCategoriesAndStore(storeId, mockCategories)).willReturn(mockProducts);

		List<Product> products = productService.findProductsByStoreAndCategory(storeId, categoryId);

		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(mockProduct, products.get(0));
	}

	@Test
	@DisplayName("상품 조회 테스트 - 상점 + 카테고리 + 옵션")
	void getProductsByStoreAndCategoryAndOptions() throws CustomException {
		Long storeId = 1L;
		Long categoryId = 1L;
		List<Long> options = List.of(1L);

		Category mockCategory = mock(Category.class);
		List<Category> mockCategories = List.of(mockCategory);
		given(categoryService.findAllSubCategories(categoryId)).willReturn(mockCategories);

		Store mockStore = mock(Store.class);
		given(mockStore.getId()).willReturn(storeId);
		given(storeRepository.findById(storeId)).willReturn(Optional.of(mockStore));

		Product mockProduct = mock(Product.class);
		List<Product> mockProducts = List.of(mockProduct);
		given(productRepository.findByStoreAndCategoryAndOptions(storeId, mockCategories, options)).willReturn(mockProducts);

		List<Product> products = productService.findProductsByStoreAndCategoryAndOptions(storeId, categoryId, options);

		assertNotNull(products);
		assertEquals(1, products.size());
		assertEquals(mockProduct, products.get(0));
	}

	@Test
	@DisplayName("상품 수정 테스트")
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