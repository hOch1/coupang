package ecommerce.coupang.domain.product.variant;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductVariantRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductVariant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_variant_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "stock_quantity", nullable = false)
	private int stockQuantity;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ProductStatus status;

	@Column(name = "is_default", nullable = false)
	private boolean isDefault = false;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductVariantOption> productVariantOptions = new ArrayList<>();

	public ProductVariant(Product product, int price, int stockQuantity, ProductStatus status, boolean isDefault) {
		this.product = product;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.status = status;
		this.isDefault = isDefault;
	}

	public static ProductVariant create(CreateProductRequest.VariantRequest request, Product product) {
		return new ProductVariant(
			product,
			request.getPrice(),
			request.getStock(),
			request.getStatus(),
			request.isDefault()
		);
	}

	public void addProductVariantOption(ProductVariantOption productVariantOption) {
		this.productVariantOptions.add(productVariantOption);
	}

	public void reduceStock(int quantity) throws CustomException {
		if (this.stockQuantity - quantity < 0)
			throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);

		this.stockQuantity -= quantity;
	}

	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	public void changeDefault(boolean change) {
		this.isDefault = change;
	}

	public void changeStock(int stockQuantity) throws CustomException {
		if (stockQuantity < 0)
			throw new CustomException(ErrorCode.INVALID_STOCK_QUANTITY);

		if (stockQuantity == 0) {
			this.status = ProductStatus.NO_STOCK;
			this.stockQuantity = 0;
		} else
			this.stockQuantity = stockQuantity;
	}

	public void changeStatus(ProductStatus status) {
		if (status.equals(ProductStatus.NO_STOCK)) {
			this.stockQuantity = 0;
			this.status = ProductStatus.NO_STOCK;
		} else
			this.status = status;
	}

	public void update(UpdateProductVariantRequest request) {
		this.price = request.getPrice() != null ? request.getPrice().intValue() : this.price;
	}

	public void delete() {
		this.isActive = false;
	}
}
