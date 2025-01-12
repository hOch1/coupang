package ecommerce.coupang.domain.product.variant;

import ecommerce.coupang.domain.category.VariantOptionValue;
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
public class ProductVariantOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_variant_option_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variant_option_value_id", nullable = false)
	private VariantOptionValue variantOptionValue;

	public ProductVariantOption(ProductVariant productVariant, VariantOptionValue variantOptionValue) {
		this.productVariant = productVariant;
		this.variantOptionValue = variantOptionValue;
	}

	public static ProductVariantOption of(ProductVariant productVariant, VariantOptionValue variantOptionValue) {
		return new ProductVariantOption(
				productVariant,
				variantOptionValue
		);
	}

	public void update(VariantOptionValue variantOptionValue) {
		this.variantOptionValue = variantOptionValue;
	}
}
