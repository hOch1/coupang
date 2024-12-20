package ecommerce.coupang.domain.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.dto.request.auth.SignupRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "phone_number", nullable = false, unique = true)
	private String phoneNumber;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@Column(name = "is_active", nullable = false)
	private boolean isActive;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();

	public Member(String name, String phoneNumber, String email, String password, MemberRole role, boolean isActive, List<Address> addresses) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.password = password;
		this.role = role;
		this.isActive = isActive;
		this.addresses = addresses;
	}

	public static Member createFromSignupRequest(SignupRequest request, PasswordEncoder passwordEncoder) {
		return new Member(
			request.getName(),
			request.getPhoneNumber(),
			request.getEmail(),
			passwordEncoder.encode(request.getPassword()),
			request.getRole(),
			true,
			new ArrayList<>()
		);
	}

	public void changeRoleSeller() throws CustomException {
		if (this.role.equals(MemberRole.SELLER))
			throw new CustomException(ErrorCode.ALREADY_ROLE_SELLER);

		this.role = MemberRole.SELLER;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Member member = (Member)o;
		return Objects.equals(id, member.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
