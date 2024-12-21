package ecommerce.coupang.domain.product.variant;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductVariantOption> productVariantOption;

	public ProductVariant(Product product, int price, int stockQuantity, ProductStatus status) {
		this.product = product;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.status = status;
	}

	public static ProductVariant create(CreateProductRequest.VariantRequest v, Product product) {
		return new ProductVariant(
				product,
				v.getPrice(),
				v.getStock(),
				v.getStatus()
		);
	}

	public void addProductVariantOption(ProductVariantOption productVariantOption) {
		this.productVariantOption.add(productVariantOption);
	}
}
