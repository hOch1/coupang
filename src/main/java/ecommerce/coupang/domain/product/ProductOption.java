package ecommerce.coupang.domain.product;

import jakarta.persistence.Column;
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
public class ProductOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_option_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_detail_id", nullable = false)
	private ProductDetail productDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_value_id", nullable = false)
	private OptionValue optionValue;

	public ProductOption(OptionValue optionValue, ProductDetail productDetail) {
		this.optionValue = optionValue;
		this.productDetail = productDetail;
		productDetail.getProductOptions().add(this);
	}

	public static ProductOption create(OptionValue optionValue, ProductDetail productDetail) {
		return new ProductOption(
			optionValue,
			productDetail
		);
	}
}
