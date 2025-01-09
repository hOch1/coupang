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
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.option.OptionRequest;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductStatusRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductStockRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductVariantRequest;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.repository.product.ProductVariantRepository;
import ecommerce.coupang.service.product.option.ProductVariantOptionService;

@ExtendWith(MockitoExtension.class)
class ProductVariantServiceTest {

	@InjectMocks
	private ProductVariantService productVariantService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductVariantRepository productVariantRepository;

	@Mock
	private ProductCreateManagement productCreateManagement;

	@Mock
	private ProductVariantOptionService productVariantOptionService;

	private Product mockProduct = mock(Product.class);
	private ProductVariant mockProductVariant = mock(ProductVariant.class);
	private Store mockStore = mock(Store.class);
	private Member mockMember = mock(Member.class);

	@BeforeEach
	public void beforeEach() {
		when(mockProductVariant.getProduct()).thenReturn(mockProduct);
		when(mockProduct.getStore()).thenReturn(mockStore);
	}

	@Test
	@DisplayName("상품 변형 추가 테스트")
	void addProductVariant() throws CustomException {
		CreateProductVariantRequest request = createProductVariantRequest();
		Long productId = 1L;

		when(productRepository.findByIdWithMemberAndCategory(productId)).thenReturn(Optional.of(mockProduct));
		doNothing().when(mockStore).validateOwner(mockMember);
		when(productCreateManagement.createVariantAndOptions(request, mockProduct)).thenReturn(mockProductVariant);

		ProductVariant productVariant = productVariantService.addProductVariant(productId, request, mockMember);

		verify(productVariantRepository).save(mockProductVariant);
		verify(mockStore).validateOwner(mockMember);
		assertThat(productVariant).isNotNull();
	}

	@Test
	@DisplayName("상품 변형 추가 테스트 - 실패 (상품 못찾음)")
	void addProductVariantFailProductNotFound() {
		CreateProductVariantRequest request = createProductVariantRequest();
		Long productId = 1L;

		when(productRepository.findByIdWithMemberAndCategory(productId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productVariantService.addProductVariant(productId, request, mockMember));

		verify(productVariantRepository, never()).save(mockProductVariant);
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	@Test
	@DisplayName("상품 변형 수정 테스트")
	void updateProductVariant() throws CustomException {
		UpdateProductVariantRequest request = updateProductVariantRequest();
		Long variantId = 1L;

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.of(mockProductVariant));
		doNothing().when(mockStore).validateOwner(mockMember);
		doNothing().when(productVariantOptionService).update(anyLong(), anyLong(), any(ProductVariant.class));

		ProductVariant productVariant = productVariantService.updateProductVariant(variantId, request, mockMember);

		verify(productVariant).update(request);
		verify(mockStore).validateOwner(mockMember);
		assertThat(productVariant).isNotNull();
	}

	@Test
	@DisplayName("상품 변형 수정 테스트 - 실패 (상품 변형 못찾음)")
	void updateProductVariantFailProductVariantNotFound() {
		UpdateProductVariantRequest request = updateProductVariantRequest();
		Long variantId = 1L;

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productVariantService.updateProductVariant(variantId, request, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	@Test
	@DisplayName("대표 상품 변경 테스트")
	void updateDefaultProduct() throws CustomException {
		Long variantId = 1L;
		Long productId = 1L;
		ProductVariant defalutProductVariant = mock(ProductVariant.class);
		when(mockProduct.getId()).thenReturn(productId);

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.of(mockProductVariant));
		doNothing().when(mockStore).validateOwner(mockMember);
		when(productVariantRepository.findByProductIdAndDefault(productId)).thenReturn(Optional.of(defalutProductVariant));

		ProductVariant productVariant = productVariantService.updateDefaultProduct(variantId, mockMember);

		verify(productVariant).changeDefault(true);
		verify(defalutProductVariant).changeDefault(false);
	}

	@Test
	@DisplayName("대표 상품 변경 테스트 - 실패 (요청 상품 변형 못찾음)")
	void updateDefaultProductFailProductNotFound() throws CustomException {
		Long variantId = 1L;

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productVariantService.updateDefaultProduct(variantId, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	@Test
	@DisplayName("대표 상품 변경 테스트 - 실패 (기존 대표상품 못찾음)")
	void updateDefaultProductFailDefaultProductNotFound() throws CustomException {
		Long variantId = 1L;
		Long productId = 1L;
		when(mockProduct.getId()).thenReturn(productId);

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.of(mockProductVariant));
		doNothing().when(mockStore).validateOwner(mockMember);
		when(productVariantRepository.findByProductIdAndDefault(productId)).thenReturn(Optional.empty());

		CustomException customException = assertThrows(CustomException.class,
			() -> productVariantService.updateDefaultProduct(variantId, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
	}

	@Test
	@DisplayName("대표 상품 변경 테스트 - 실패 (요청한 상품이 이미 대표상품임)")
	void updateDefaultProductFailAlreadyDefault() throws CustomException {
		Long variantId = 1L;
		Long productId = 1L;
		when(mockProduct.getId()).thenReturn(productId);

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.of(mockProductVariant));
		doNothing().when(mockStore).validateOwner(mockMember);
		when(productVariantRepository.findByProductIdAndDefault(productId)).thenReturn(Optional.of(mockProductVariant));

		CustomException customException = assertThrows(CustomException.class,
			() -> productVariantService.updateDefaultProduct(variantId, mockMember));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.ALREADY_DEFAULT_PRODUCT);
	}

	@Test
	@DisplayName("상품 재고 변경 테스트")
	void updateProductStock() throws CustomException {
		UpdateProductStockRequest request = new UpdateProductStockRequest(1);
		Long variantId = 1L;

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.of(mockProductVariant));
		doNothing().when(mockStore).validateOwner(mockMember);

		ProductVariant productVariant = productVariantService.updateProductStock(variantId, request, mockMember);

		verify(productVariant).changeStock(request.getStockQuantity());
	}

	@Test
	@DisplayName("상품 상태 변경 테스트")
	void updateProductStatus() throws CustomException {
		UpdateProductStatusRequest request = new UpdateProductStatusRequest(ProductStatus.INACTIVE);
		Long variantId = 1L;

		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.of(mockProductVariant));
		doNothing().when(mockStore).validateOwner(mockMember);

		ProductVariant productVariant = productVariantService.updateProductStatus(variantId, request, mockMember);

		verify(productVariant).changeStatus(request.getStatus());
	}

	@Test
	@DisplayName("상품 변형 삭제 테스트")
	void deleteProductVariant() throws CustomException {
		Long variantId = 1L;
		when(productVariantRepository.findByIdWithMember(variantId)).thenReturn(Optional.of(mockProductVariant));
		doNothing().when(mockStore).validateOwner(mockMember);

		ProductVariant productVariant = productVariantService.deleteProductVariant(variantId, mockMember);

		verify(productVariant).delete();
		assertThat(productVariant.isActive()).isFalse();
	}

	private UpdateProductVariantRequest updateProductVariantRequest() {
		OptionRequest optionRequest = new OptionRequest(
			1L,
			1L
		);
		return new UpdateProductVariantRequest(
			1000L,
			List.of(optionRequest)
		);
	}

	private CreateProductVariantRequest createProductVariantRequest() {
		return new CreateProductVariantRequest(
			1000,
			100,
			ProductStatus.ACTIVE,
			true,
			List.of(mock(OptionRequest.class))
		);
	}
}