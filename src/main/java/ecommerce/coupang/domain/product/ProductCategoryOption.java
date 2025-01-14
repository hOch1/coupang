package ecommerce.coupang.domain.product;

import ecommerce.coupang.domain.category.CategoryOptionValue;
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
public class ProductCategoryOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_category_option_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_option_value_id", nullable = false)
	private CategoryOptionValue categoryOptionValue;

	public ProductCategoryOption(Product product, CategoryOptionValue categoryOptionValue) {
		this.product = product;
		this.categoryOptionValue = categoryOptionValue;
	}

	public static ProductCategoryOption of(Product product, CategoryOptionValue categoryOptionValue) {
		return new ProductCategoryOption(
				product,
				categoryOptionValue
		);
	}

	/* 옵션 수정 */
	public void update(CategoryOptionValue categoryOptionValue) {
		this.categoryOptionValue = categoryOptionValue;
	}
}
