package ecommerce.coupang.service.product.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
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
import ecommerce.coupang.domain.product.ProductStatus;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.OptionValueRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryService categoryService;

	@Mock
	private OptionValueRepository optionValueRepository;

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	@DisplayName("상품 등록 테스트")
	void createProductTest() throws CustomException {
		Map<Long, Long> options = new HashMap<>();
		options.put(1L, 1L);
		options.put(2L, 8L);
		options.put(3L, 9L);

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
		OptionValue mockOptionValue1 = mock(OptionValue.class);
		OptionValue mockOptionValue2 = mock(OptionValue.class);
		OptionValue mockOptionValue3 = mock(OptionValue.class);

		when(storeRepository.findById(request.getStoreId())).thenReturn(Optional.of(mockStore));
		when(categoryService.findBottomCategory(request.getCategoryId())).thenReturn(mockCategory);
		when(optionValueRepository.findById(1L)).thenReturn(Optional.of(mockOptionValue1));
		when(optionValueRepository.findById(8L)).thenReturn(Optional.of(mockOptionValue2));
		when(optionValueRepository.findById(9L)).thenReturn(Optional.of(mockOptionValue3));
		when(productRepository.save(any(Product.class))).thenReturn(mockProduct);

		Long saveProductId = productService.createProduct(request, mockMember);

		assertThat(saveProductId).isEqualTo(mockProduct.getId());

		verify(productRepository).save(any(Product.class));
		verify(storeRepository).findById(request.getStoreId());
	}
}