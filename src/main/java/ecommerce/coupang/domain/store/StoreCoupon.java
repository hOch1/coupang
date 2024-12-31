package ecommerce.coupang.domain.store;

import java.time.LocalDateTime;
import java.util.Objects;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Member;
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
public class StoreCoupon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_coupon_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "coupon_code", unique = true, nullable = false)
	private String couponCode;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "coupon_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private CouponType type;

	@Column(name = "discount_value", nullable = false)
	private int discountValue;

	@Column(name = "min_price", nullable = false)
	private int minPrice = 0;

	@Column(name = "limit_discount_price", nullable = false)
	private int limitDiscountPrice = Integer.MAX_VALUE;

	@Column(name = "coupon_stock", nullable = false)
	private int couponStock = Integer.MAX_VALUE;

	@Column(name = "limit_date", nullable = false)
	private LocalDateTime limitDate = LocalDateTime.MAX;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		StoreCoupon that = (StoreCoupon)o;
		return Objects.equals(id, that.id) && Objects.equals(couponCode, that.couponCode);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(id);
		result = 31 * result + Objects.hashCode(couponCode);
		return result;
	}
}