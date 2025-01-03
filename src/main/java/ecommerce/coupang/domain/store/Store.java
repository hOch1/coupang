package ecommerce.coupang.domain.store;

import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
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
public class Store extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "store_number", nullable = false, unique = true)
	private String storeNumber;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> products = new ArrayList<>();

	public Store(String name, String description, String storeNumber, Member member) {
		this.name = name;
		this.description = description;
		this.storeNumber = storeNumber;
		this.member = member;
	}

	public static Store createFromRequest(CreateStoreRequest request, Member member) {
		return new Store(
			request.getName(),
			request.getDescription(),
			request.getStoreNumber(),
			member
		);
	}

	public void update(UpdateStoreRequest request) {
		this.name = request.getName() != null ? request.getName() : this.name;
		this.description = request.getDescription() != null ? request.getDescription() : this.description;
	}

	public void delete() {
		products.forEach(Product::delete);
		this.isActive = false;
	}
}
