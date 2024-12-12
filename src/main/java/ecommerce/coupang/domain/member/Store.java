package ecommerce.coupang.domain.member;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.dto.request.store.CreateStoreRequest;
import ecommerce.coupang.dto.request.store.UpdateStoreRequest;
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
public class Store extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "store_number", nullable = false, unique = true)
	private String storeNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

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
		if (request.getName() != null)
			this.name = request.getName();
		if (request.getDescription() != null)
			this.description = request.getDescription();
	}
}
