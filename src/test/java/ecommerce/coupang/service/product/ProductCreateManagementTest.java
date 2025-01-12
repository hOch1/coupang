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
	private ProductVariantOptionService productVariantOptionService;

	@Mock
	private ProductCategoryOptionService productCategoryOptionService;

	@Mock
	private CategoryOptionService categoryOptionService;

	@Mock
	private VariantOptionService variantOptionService;

	private Store mockStore = mock(Store.class);
	private Category mockCategory = mock(Category.class);
	private Product mockProduct = mock(Product.class);


	@Test
	@DisplayName("상품 생성 후 상품 변형, 옵션 추가")
	void createProductAndVariantAndOptions() throws CustomException {
		CreateProductRequest request = createProductRequest();

		try (MockedStatic<Product> mockedStatic = mockStatic(Product.class)) {
			when(categoryOptionService.getCategoryOption(anyLong())).thenReturn(List.of(mock(CategoryOption.class)));
			when(variantOptionService.getVariantOption(anyLong())).thenReturn(List.of(mock(VariantOption.class)));
			Product mockProduct = mock(Product.class);
			mockedStatic.when(() -> Product.create(request, mockStore, mockCategory))
				.thenReturn(mockProduct);

			Product product = productCreateManagement.createProductAndVariantAndOptions(request, mockStore, mockCategory);
			assertThat(product).isNotNull();
		}

		// when(Product.create(request, mockStore, mockCategory)).thenReturn(mockProduct);
		// when(categoryOptionService.getCategoryOption(anyLong())).thenReturn(List.of(mock(CategoryOption.class)));
		// when(variantOptionService.getVariantOption(anyLong())).thenReturn(List.of(mock(VariantOption.class)));


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

}