package ecommerce.coupang.service.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.domain.category.VariantOption;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductCategoryOption;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.domain.product.variant.ProductVariantOption;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.option.OptionRequest;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.service.category.CategoryOptionService;
import ecommerce.coupang.service.category.VariantOptionService;
import ecommerce.coupang.service.product.option.ProductCategoryOptionService;
import ecommerce.coupang.service.product.option.ProductVariantOptionService;

@ExtendWith(MockitoExtension.class)
class ProductCreateManagementTest {

	@InjectMocks
	private ProductCreateManagement productCreateManagement;

	@Mock
	private CategoryOptionService categoryOptionService;

	@Mock
	private VariantOptionService variantOptionService;

	@Mock
	private ProductCategoryOptionService productCategoryOptionService;

	@Mock
	private ProductVariantOptionService productVariantOptionService;

	private Store mockStore = mock(Store.class);
	private Category mockCategory = mock(Category.class);
	private Product mockProduct = mock(Product.class);
	private ProductVariant mockProductVariant = mock(ProductVariant.class);
	private CategoryOption mockCategoryOption = mock(CategoryOption.class);
	private VariantOption mockVariantOption = mock(VariantOption.class);


	@Test
	@DisplayName("상품 생성 후 상품 변형, 옵션 추가")
	void createProductAndVariantAndOptions() throws CustomException {
		CreateProductRequest request = createProductRequest();

		when(categoryOptionService.getCategoryOption(anyLong())).thenReturn(List.of(mockCategoryOption));
		when(mockCategoryOption.getId()).thenReturn(1L);
		when(variantOptionService.getVariantOption(anyLong())).thenReturn(List.of(mockVariantOption));
		when(mockVariantOption.getId()).thenReturn(1L);

		try (MockedStatic<Product> mockedStatic = mockStatic(Product.class)) {
			when(mockProduct.getCategory()).thenReturn(mockCategory);
			mockedStatic.when(() -> Product.of(request, mockStore, mockCategory))
				.thenReturn(mockProduct);

			Product product = productCreateManagement.createProductAndVariantAndOptions(request, mockStore, mockCategory);
			assertThat(product).isNotNull();
		}
	}

	@Test
	@DisplayName("상품 변형 생성 후 옵션 추가")
	void createVariantAndOptions() throws CustomException {
		CreateProductVariantRequest request = createProductVariantRequest();

		when(variantOptionService.getVariantOption(anyLong())).thenReturn(List.of(mockVariantOption));
		when(mockVariantOption.getId()).thenReturn(1L);

		try (MockedStatic<ProductVariant> mockedStatic = mockStatic(ProductVariant.class)) {
			when(mockProduct.getCategory()).thenReturn(mockCategory);
			mockedStatic.when(() -> ProductVariant.of(request, mockProduct))
				.thenReturn(mockProductVariant);

			ProductVariant productVariant = productCreateManagement.createVariantAndOptions(request, mockProduct);
			assertThat(productVariant).isNotNull();
		}
	}

	@Test
	@DisplayName("상품 카테고리 옵션 추가")
	void addCategoryOptionsToProduct() throws CustomException {
		when(productCategoryOptionService.createProductCategoryOption(1L, mockProduct))
			.thenReturn(mock(ProductCategoryOption.class));

		productCreateManagement.addCategoryOptionsToProduct(1L, mockProduct);

		verify(mockProduct).addProductOptions(any(ProductCategoryOption.class));
	}

	@Test
	@DisplayName("상품 변형 변형 옵션 추가")
	void addVariantOptionToProductVariant() throws CustomException {
		when(productVariantOptionService.createProductVariantOption(1L, mockProductVariant))
			.thenReturn(mock(ProductVariantOption.class));

		productCreateManagement.addVariantOptionToProductVariant(1L, mockProductVariant);

		verify(mockProductVariant).addProductVariantOptions(any(ProductVariantOption.class));
	}

	private CreateProductRequest createProductRequest() {
		return new CreateProductRequest(
			"name",
			"description",
			1L,
			List.of(optionRequest()),
			List.of(createProductVariantRequest())
		);
	}

	private CreateProductVariantRequest createProductVariantRequest() {
		return new CreateProductVariantRequest(
			1000,
			100,
			ProductStatus.ACTIVE,
			true,
			List.of(optionRequest())
		);
	}

	private OptionRequest optionRequest() {
		return new OptionRequest(
			1L,
			1L
		);
	}

}