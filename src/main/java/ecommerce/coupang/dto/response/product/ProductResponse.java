package ecommerce.coupang.dto.response.product;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.sub.ProductSubOption;
import ecommerce.coupang.domain.product.variant.ProductStatus;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final StoreResponse store;
	private final CategoryResponse category;
	private final List<ProductDetailResponse> details;

	public static ProductResponse from(Product product) {
		// return new ProductResponse(
		// 	product.getId(),
		// 	product.getName(),
		// 	product.getDescription(),
		// 	StoreResponse.from(product.getStore(), false),
		// 	CategoryResponse.from(product.getCategory(), false),
		// 	product.getProductDetails().stream()
		// 		.map(ProductDetailResponse::from)
		// 		.toList()
		// );
		return null;
	}

	@Getter
	@AllArgsConstructor
	private static class ProductDetailResponse {

		private final Long id;
		private final int price;
		private final int stockQuantity;
		private final ProductStatus status;
		private final List<ProductOptionResponse> options;

		// private static ProductDetailResponse from(ProductDetail productDetail) {
		// 	return new ProductDetailResponse(
		// 		productDetail.getId(),
		// 		productDetail.getPrice(),
		// 		productDetail.getStockQuantity(),
		// 		productDetail.getStatus(),
		// 		productDetail.getProductOptions().stream()
		// 			.map(ProductOptionResponse::from)
		// 			.toList()
		// 	);
		// }
	}

	@Getter
	@AllArgsConstructor
	private static class ProductOptionResponse {
		private Long id;
		private final String name;
		private final String value;

		private static ProductOptionResponse from(ProductSubOption productSubOption) {
			return new ProductOptionResponse(
				productSubOption.getId(),
				productSubOption.getSubOptionValue().getCategorySubOption().getOptionName(),
				productSubOption.getSubOptionValue().getValue()
			);
		}
	}
}
