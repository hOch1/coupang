package ecommerce.coupang.service.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.dto.request.product.option.OptionRequest;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.category.CategoryService;
import ecommerce.coupang.service.product.option.ProductCategoryOptionService;
import ecommerce.coupang.service.store.query.StoreQueryService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryService categoryService;

	@Mock
	private StoreQueryService storeQueryService;

	@Mock
	private ProductCreateManagement productCreateManagement;

	@Mock
	private ProductCategoryOptionService productCategoryOptionService;

	private final Category mockCategory = mock(Category.class);
	private final Store mockStore = mock(Store.class);
	private final Member mockMember = mock(Member.class);
	private final Product mockProduct = mock(Product.class);

	@BeforeEach
	public void beforeEach() {
		when(mockProduct.getStore()).thenReturn(mockStore);
	}

	@Test
	@DisplayName("상품 생성 테스트")
	void createProduct() throws CustomException {
		CreateProductRequest request = createProductRequest();
		Long storeId = 1L;

		when(categoryService.findBottomCategory(request.getCategoryId())).thenReturn(mockCategory);
		when(storeQueryService.findStore(storeId)).thenReturn(mockStore);
		doNothing().when(mockStore).validateOwner(mockMember);
		when(productCreateManagement.createProductAndVariantAndOptions(request, mockStore, mockCategory))
			.thenReturn(mockProduct);

		Product product = productService.createProduct(request, storeId, mockMember);

		verify(productRepository).save(mockProduct);
		verify(mockStore).validateOwner(mockMember);
		assertThat(product).isNotNull();
	}

	@Test
	@DisplayName("상품 수정 테스트")
	void updateProduct() throws CustomException {
		Long productId = 1L;
		UpdateProductRequest request = updateProductRequest();

		when(productRepository.findByIdWithMemberAndCategory(productId)).thenReturn(Optional.of(mockProduct));
		doNothing().when(mockStore).validateOwner(mockMember);
		doNothing().when(productCategoryOptionService).update(anyLong(), anyLong(), any(Product.class));

		Product product = productService.updateProduct(productId, request, mockMember);

		verify(product).update(request);
		verify(mockStore).validateOwner(mockMember);
	}

	@Test
	@DisplayName("상품 수정 테스트 - 실패 (상품 못찾음)")
	void updateProductFailProductNotFound() {
		Long productId = 1L;
		UpdateProductRequest request = updateProductRequest();

		when(productRepository.findByIdWithMemberAndCategory(productId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.updateProduct(productId, request, mockMember));

		verify(productRepository).findByIdWithMemberAndCategory(productId);
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	@Test
	@DisplayName("상품 삭제 테스트")
	void deleteProduct() throws CustomException {
		Long productId = 1L;

		when(productRepository.findByIdWithMemberAndCategory(productId)).thenReturn(Optional.of(mockProduct));
		doNothing().when(mockStore).validateOwner(mockMember);

		Product product = productService.deleteProduct(productId, mockMember);

		verify(product).delete();
		assertThat(product.isActive()).isFalse();
	}

	@Test
	@DisplayName("상품 삭제 테스트 - 실패 (상품 못찾음)")
	void deleteProductFailProductNotFound() {
		Long productId = 1L;

		when(productRepository.findByIdWithMemberAndCategory(productId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productService.deleteProduct(productId, mockMember));

		verify(productRepository).findByIdWithMemberAndCategory(productId);
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	private CreateProductRequest createProductRequest() {
		return new CreateProductRequest(
			"name",
			"description",
			1L,
			List.of(mock(OptionRequest.class)),
			List.of(mock(CreateProductVariantRequest.class))
		);
	}

	private UpdateProductRequest updateProductRequest() {
		OptionRequest optionRequest = new OptionRequest(
			1L,
			1L
		);
		return new UpdateProductRequest(
			"update name",
			"update description",
			List.of(optionRequest)
		);
	}
}