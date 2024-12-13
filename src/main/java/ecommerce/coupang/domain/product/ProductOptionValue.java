package ecommerce.coupang.domain.product;

import ecommerce.coupang.dto.request.product.CreateProductRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductOptionValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_detail_id", nullable = false)
	private ProductDetail productDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_value_id", nullable = false)
	private OptionValue optionValue;

	public ProductOptionValue(OptionValue optionValue, ProductDetail productDetail) {
		this.optionValue = optionValue;
		this.productDetail = productDetail;
	}

	public static ProductOptionValue create(OptionValue optionValue, ProductDetail productDetail) {
		return new ProductOptionValue(
			optionValue,
			productDetail
		);
	}
}
