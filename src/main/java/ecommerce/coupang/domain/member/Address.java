package ecommerce.coupang.domain.member;

import java.util.Objects;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.dto.request.member.AddAddressRequest;
import ecommerce.coupang.dto.request.member.UpdateAddressRequest;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Address extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long id;

	@Column(name = "address", nullable = false)
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private AddressType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "is_default", nullable = false)
	private boolean isDefault;

	public Address(String address, AddressType type, Member member, boolean isDefault) {
		this.address = address;
		this.type = type;
		this.isDefault = isDefault;
		this.member = member;
	}

	public static Address createFromRequest(AddAddressRequest request, Member member, boolean isDefault) {
		return new Address(request.getAddress(), request.getType(), member, isDefault);
	}

	public void update(UpdateAddressRequest request) {
		if (request.getAddress() != null && !request.getAddress().trim().isEmpty())
			this.address = request.getAddress().trim();
		if (request.getType() != null)
			this.type = request.getType();
	}

	public void unsetAsDefault() {
		this.isDefault = false;
	}

	public void setAsDefault() {
		this.isDefault = true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Address address = (Address)o;
		return Objects.equals(id, address.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
