package ecommerce.coupang.domain.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_detail_id")
	private Long id;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "stock_quantity", nullable = false)
	private int stockQuantity;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ProductStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductOption> productOptions = new ArrayList<>();

	public ProductDetail(int price, int stockQuantity, ProductStatus status, Product product) {
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.status = status;
		this.product = product;
	}

	public static ProductDetail create(CreateProductRequest.CreateDetailRequest request, Product product) {
		ProductDetail productDetail = new ProductDetail(
			request.getPrice(),
			request.getStockQuantity(),
			request.getStatus(),
			product
		);

		product.getProductDetails().add(productDetail);
		return productDetail;
	}

	public void order(int quantity) throws CustomException {
		if (this.stockQuantity - quantity < 0)
			throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);

		this.stockQuantity -= quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProductDetail that = (ProductDetail)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
