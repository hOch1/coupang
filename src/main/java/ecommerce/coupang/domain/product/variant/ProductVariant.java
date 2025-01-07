package ecommerce.coupang.domain.product.variant;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.variant.CreateProductVariantRequest;
import ecommerce.coupang.dto.request.product.variant.UpdateProductVariantRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
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
@Table(indexes = {
	@Index(name = "idx_product_variant_default", columnList = "is_default, product_id"),
	@Index(name = "idx_product_variant_price", columnList = "is_default, price"),
	@Index(name = "idx_product_variant_sales", columnList = "is_default, sales_count")
})
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

	@Column(name = "sales_count", nullable = false)
	private int salesCount = 0;

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

	public static ProductVariant create(CreateProductVariantRequest request, Product product) {
		return new ProductVariant(
			product,
			request.getPrice(),
			request.getStock(),
			request.getStatus(),
			request.isDefault()
		);
	}

	public void addProductVariantOptions(ProductVariantOption productVariantOption) {
		this.productVariantOptions.add(productVariantOption);
	}

	/* 재고 감소 */
	public void reduceStock(int quantity) throws CustomException {
		validateStockQuantity(quantity);
		this.stockQuantity -= quantity;
	}

	/* 재고 추가 */
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	/* 대표 상품 변경 */
	public void changeDefault(boolean change) {
		this.isDefault = change;
	}

	/* 판매량 증가 */
	public void increaseSalesCount(int quantity) {
		this.salesCount += quantity;
	}

	/* 판매량 감소 */
	public void decreaseSalesCount(int quantity) {
		this.salesCount -= quantity;
	}

	/* 상품 재고 변경 */
	public void changeStock(int quantity) throws CustomException {
		if (quantity < 0) // 음수 예외
			throw new CustomException(ErrorCode.INVALID_STOCK_QUANTITY);

		/* 재고 0 으로 수정시 status 재고없음으로 수정 */
		if (quantity == 0) {
			this.status = ProductStatus.NO_STOCK;
			this.stockQuantity = 0;
		} else
			this.stockQuantity = quantity;
	}

	/* 상품 상태 변경 */
	public void changeStatus(ProductStatus status) {
		/* 재고 없음으로 변경시 상품 재고 0으로 수정 */
		if (status.equals(ProductStatus.NO_STOCK)) {
			this.stockQuantity = 0;
			this.status = ProductStatus.NO_STOCK;
		} else
			this.status = status;
	}

	/* 상품 변형 수정 */
	public void update(UpdateProductVariantRequest request) {
		this.price = request.getPrice() != null ? request.getPrice().intValue() : this.price;
	}

	/* 상품 변형 삭제 */
	public void delete() {
		this.isActive = false;
	}

	private void validateStockQuantity(int quantity) throws CustomException {
		if (this.stockQuantity - quantity < 0) {
			throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);
		}
	}
}
