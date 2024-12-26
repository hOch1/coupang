package ecommerce.coupang.service.product.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.CategoryOptionValue;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.VariantOptionValue;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.category.CategoryOptionValueRepository;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.repository.product.VariantOptionValueRepository;
import ecommerce.coupang.service.product.CategoryService;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private CategoryService categoryService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductVariantRepository productVariantRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private CategoryOptionValueRepository categoryOptionValueRepository;

	@Mock
	private VariantOptionValueRepository variantOptionValueRepository;

	private CreateProductRequest request;
	private Member mockMember;
	private Store mockStore;
	private Category mockCategory;
	private Product mockProduct;
	private ProductVariant mockProductVariant;

	@BeforeEach
	public void beforeEach() {
		request = createRequest();
		mockMember = mock(Member.class);
		mockStore = mock(Store.class);
		mockCategory = mock(Category.class);
		mockProduct = mock(Product.class);
		mockProductVariant = mock(ProductVariant.class);
	}

	@Test
	@DisplayName("상품 등록 테스트")
	void createProduct() throws CustomException {
		when(categoryService.findBottomCategory(1L)).thenReturn(mockCategory);
		when(storeRepository.findByIdWithMember(1L)).thenReturn(Optional.of(mockStore));
		when(mockStore.getMember()).thenReturn(mockMember);
		CategoryOptionValue mockCategoryOptionValue = mock(CategoryOptionValue.class);
		when(categoryOptionValueRepository.findById(1L)).thenReturn(Optional.of(mockCategoryOptionValue));
		VariantOptionValue mockVariantOptionValue = mock(VariantOptionValue.class);
		when(variantOptionValueRepository.findById(1L)).thenReturn(Optional.of(mockVariantOptionValue));

		Product product = productService.createProduct(request, mockMember);

		verify(categoryService).findBottomCategory(anyLong());
		verify(storeRepository).findByIdWithMember(anyLong());
		verify(categoryOptionValueRepository).findById(anyLong());
		verify(variantOptionValueRepository).findById(anyLong());
		verify(productRepository).save(any(Product.class));

		assertThat(product).isNotNull();
		assertThat(product.getName()).isEqualTo(request.getName());
		assertThat(product.getDescription()).isEqualTo(request.getDescription());
		assertThat(product.getProductOptions().size()).isEqualTo(1);
		assertThat(product.getProductVariants().size()).isEqualTo(1);
	}

	@Test
	@DisplayName("상품 등록 테스트 - 실패 (상점을 찾을 수 없음)")
	void createProductFailStoreNotFound() throws CustomException {
		when(categoryService.findBottomCategory(1L)).thenReturn(mockCategory);
		when(storeRepository.findByIdWithMember(1L)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.createProduct(request, mockMember));

		verify(categoryService).findBottomCategory(anyLong());
		verify(storeRepository).findByIdWithMember(anyLong());

		verify(categoryOptionValueRepository, never()).findById(anyLong());
		verify(variantOptionValueRepository, never()).findById(anyLong());
		verify(productRepository, never()).save(any(Product.class));

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.STORE_NOT_FOUND);
	}

	@Test
	@DisplayName("상품 등록 테스트 - 실패 (상점 주인과 요청 회원이 다름)")
	void createProductFailStoreMemberNotMatch() throws CustomException {
		when(categoryService.findBottomCategory(1L)).thenReturn(mockCategory);
		when(storeRepository.findByIdWithMember(1L)).thenReturn(Optional.of(mockStore));
		Member mockStoreMember = mock(Member.class);
		when(mockStore.getMember()).thenReturn(mockStoreMember);

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.createProduct(request, mockMember));

		verify(categoryService).findBottomCategory(anyLong());
		verify(storeRepository).findByIdWithMember(anyLong());

		verify(categoryOptionValueRepository, never()).findById(anyLong());
		verify(variantOptionValueRepository, never()).findById(anyLong());
		verify(productRepository, never()).save(any(Product.class));

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
	}

	@Test
	@DisplayName("상품 등록 테스트 - 실패 (카테고리 옵션을 찾지 못함)")
	void createProductFailCategoryOptionNotFound() throws CustomException {
		when(categoryService.findBottomCategory(1L)).thenReturn(mockCategory);
		when(storeRepository.findByIdWithMember(1L)).thenReturn(Optional.of(mockStore));
		when(mockStore.getMember()).thenReturn(mockMember);
		when(categoryOptionValueRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.createProduct(request, mockMember));

		verify(categoryService).findBottomCategory(anyLong());
		verify(storeRepository).findByIdWithMember(anyLong());
		verify(categoryOptionValueRepository).findById(anyLong());

		verify(variantOptionValueRepository, never()).findById(anyLong());
		verify(productRepository, never()).save(any(Product.class));

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.OPTION_VALUE_NOT_FOUND);
	}

	@Test
	@DisplayName("상품 등록 테스트 - 실패 (변형 옵션을 찾기 못함)")
	void createProductFailVariantOptionNotFound() throws CustomException {
		when(categoryService.findBottomCategory(1L)).thenReturn(mockCategory);
		when(storeRepository.findByIdWithMember(1L)).thenReturn(Optional.of(mockStore));
		when(mockStore.getMember()).thenReturn(mockMember);
		CategoryOptionValue mockCategoryOptionValue = mock(CategoryOptionValue.class);
		when(categoryOptionValueRepository.findById(1L)).thenReturn(Optional.of(mockCategoryOptionValue));
		when(variantOptionValueRepository.findById(1L)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.createProduct(request, mockMember));

		verify(categoryService).findBottomCategory(anyLong());
		verify(storeRepository).findByIdWithMember(anyLong());
		verify(categoryOptionValueRepository).findById(anyLong());
		verify(variantOptionValueRepository).findById(anyLong());

		verify(productRepository, never()).save(any(Product.class));

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.OPTION_VALUE_NOT_FOUND);
	}

	@Test
	@DisplayName("카테고리로 상품 조회 테스트")
	void findByCategory() throws CustomException {
		Long categoryId = 1L;

		when(categoryService.findAllSubCategories(categoryId)).thenReturn(List.of(mockCategory));
		when(productRepository.findByCategories(List.of(mockCategory))).thenReturn(List.of(mockProduct));
		when(productVariantRepository.findByProducts(List.of(mockProduct))).thenReturn(List.of(mockProductVariant));

		List<ProductVariant> productVariants = productService.findProductsByCategory(categoryId);

		verify(categoryService).findAllSubCategories(categoryId);
		verify(productRepository).findByCategories(List.of(mockCategory));
		verify(productVariantRepository).findByProducts(List.of(mockProduct));

		assertThat(productVariants.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("상점으로 상품 조회 테스트")
	void findByStore() throws CustomException {
		Long storeId = 1L;
		when(mockStore.getId()).thenReturn(storeId);

		when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));
		when(productRepository.findByStore(storeId)).thenReturn(List.of(mockProduct));
		when(productVariantRepository.findByProducts(List.of(mockProduct))).thenReturn(List.of(mockProductVariant));

		List<ProductVariant> productVariants = productService.findProductsByStore(storeId);

		verify(storeRepository).findById(storeId);
		verify(productRepository).findByStore(storeId);
		verify(productVariantRepository).findByProducts(List.of(mockProduct));

		assertThat(productVariants.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("대표 상품 변경 테스트")
	void changeDefaultProduct() throws CustomException {
		when(mockStore.getMember()).thenReturn(mockMember);
		when(mockProduct.getStore()).thenReturn(mockStore);
		when(mockProductVariant.getProduct()).thenReturn(mockProduct);
		ProductVariant defaultProduct = mock(ProductVariant.class);

		when(productVariantRepository.findByIdWithMember(anyLong())).thenReturn(Optional.of(mockProductVariant));
		when(productVariantRepository.findByProductIdAndDefault(anyLong())).thenReturn(Optional.of(defaultProduct));

		ProductVariant productVariant = productService.updateDefaultProduct(anyLong(), mockMember);

		verify(productVariantRepository).findByIdWithMember(anyLong());
		verify(productVariantRepository).findByProductIdAndDefault(anyLong());
		verify(productVariant).changeDefault(true);
		verify(defaultProduct).changeDefault(false);
	}

	@Test
	@DisplayName("대표 상품 변경 테스트 - 실패 (해당 상품 등록한 회원과 요청한 회원이 다름)")
	void changeDefaultProductFailMemberNotMatch() {
		when(mockStore.getMember()).thenReturn(mockMember);
		when(mockProduct.getStore()).thenReturn(mockStore);
		when(mockProductVariant.getProduct()).thenReturn(mockProduct);
		Member otherMember = mock(Member.class);

		when(productVariantRepository.findByIdWithMember(anyLong())).thenReturn(Optional.of(mockProductVariant));

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.updateDefaultProduct(anyLong(), otherMember));

		verify(productVariantRepository).findByIdWithMember(anyLong());
		verify(productVariantRepository, never()).findByProductIdAndDefault(anyLong());

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
	}

	@Test
	@DisplayName("대표 상품 변경 테스트 - 실패 (요청한 상품이 이미 대표 상품)")
	void changeDefaultProductFailAlreadyDefault() {
		when(mockStore.getMember()).thenReturn(mockMember);
		when(mockProduct.getStore()).thenReturn(mockStore);
		when(mockProductVariant.getProduct()).thenReturn(mockProduct);

		when(productVariantRepository.findByIdWithMember(anyLong())).thenReturn(Optional.of(mockProductVariant));
		when(productVariantRepository.findByProductIdAndDefault(anyLong())).thenReturn(Optional.of(mockProductVariant));

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.updateDefaultProduct(anyLong(), mockMember));

		verify(productVariantRepository).findByIdWithMember(anyLong());
		verify(productVariantRepository).findByProductIdAndDefault(anyLong());

		assertThat(customException).isNotNull();
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ALREADY_DEFAULT_PRODUCT);
	}


	private CreateProductRequest createRequest() {
		CreateProductRequest.CategoryOptionsRequest categoryOptionsRequest = new CreateProductRequest.CategoryOptionsRequest(
			1L,
			1L
		);

		CreateProductRequest.VariantRequest.VariantOption variantOption = new CreateProductRequest.VariantRequest.VariantOption(
			1L,
			1L
		);

		CreateProductRequest.VariantRequest variantRequest = new CreateProductRequest.VariantRequest(
			10000,
			10,
			ProductStatus.ACTIVE,
			true,
			List.of(variantOption)
		);

		return new CreateProductRequest(
			"ProductA",
			"Test Product",
			1L,
			1L,
			List.of(categoryOptionsRequest),
			List.of(variantRequest)
		);
	}
}