package ecommerce.coupang.dto.response.product;

import java.util.List;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.domain.product.ProductStatus;
import ecommerce.coupang.dto.response.store.StoreResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

	private final Long id;
	private final String name;
	private final String description;
	private final StoreResponse store;
	private final String category;
	private final List<ProductDetailResponse> details;

	public static ProductResponse from(Product product) {
		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getDescription(),
			StoreResponse.fromProductResponse(product.getStore()),
			product.getCategory().getName(),
			product.getProductDetails().stream()
				.map(ProductDetailResponse::from)
				.toList()
		);
	}

	@Getter
	@AllArgsConstructor
	public static class ProductDetailResponse {

		private final int price;
		private final int stockQuantity;
		private final ProductStatus status;
		// private final OptionResponse option;

		public static ProductDetailResponse from(ProductDetail productDetail) {
			return new ProductDetailResponse(
				productDetail.getPrice(),
				productDetail.getStockQuantity(),
				productDetail.getStatus()
				// OptionResponse.from(productDetail.getProductOptions())
			);
		}
	}
}
