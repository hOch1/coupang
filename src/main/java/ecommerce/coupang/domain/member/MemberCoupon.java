package ecommerce.coupang.domain.member;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.store.Coupon;
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
public class MemberCoupon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_coupon_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_coupon_id", nullable = false)
	private Coupon coupon;

	@Column(name = "is_used", nullable = false)
	private boolean isUsed = false;

	public MemberCoupon(Member member, Coupon coupon) {
		this.member = member;
		this.coupon = coupon;
	}

	public static MemberCoupon create(Member member, Coupon coupon) {
		return new MemberCoupon(
			member,
			coupon
		);
	}

	public void use() {
		this.isUsed = true;
	}
}
