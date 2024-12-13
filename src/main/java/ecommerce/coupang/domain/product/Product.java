package ecommerce.coupang.domain.product;

import java.util.List;
import java.util.Objects;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetail> productDetails;

	public Product(String name, String description, Store store, Category category) {
		this.name = name;
		this.description = description;
		this.store = store;
		this.category = category;
	}

	public static Product create(CreateProductRequest request, Store store, Category category) {
		return new Product(
			request.getName(),
			request.getDescription(),
			store,
			category
		);
	}

	public void update(UpdateProductRequest request) {
		// TODO
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Product product = (Product)o;
		return Objects.equals(id, product.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
