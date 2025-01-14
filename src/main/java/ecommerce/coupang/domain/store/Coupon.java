package ecommerce.coupang.domain.store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.dto.request.store.coupon.CreateCouponRequest;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coupon_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

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

	@OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CouponProduct> couponProducts = new ArrayList<>();

	public Coupon(Store store, String name, String description, CouponType type, int discountValue,
		int minPrice, int limitDiscountPrice, int couponStock, LocalDateTime limitDate) {

		this.store = store;
		this.name = name;
		this.description = description;
		this.type = type;
		this.discountValue = discountValue;
		this.minPrice = minPrice;
		this.limitDiscountPrice = limitDiscountPrice;
		this.couponStock = couponStock;
		this.limitDate = limitDate;
	}

	public static Coupon of(CreateCouponRequest request, Store store) {
		return new Coupon(
			store,
			request.getName(),
			request.getDescription(),
			request.getType(),
			request.getDiscountValue(),
			request.getMinPrice(),
			request.getLimitDiscountPrice(),
			request.getCouponStock(),
			request.getLimitDate()
		);
	}

	public void addCouponProducts(CouponProduct couponProduct) {
		this.couponProducts.add(couponProduct);
	}

	public void reduceStock() {
		this.couponStock--;
	}

	public boolean validateMinPrice(int price) {
		return price > this.minPrice;
	}

	public int calculateDiscount(int price) {
		return switch (this.type) {
			case FIXED -> this.discountValue;
			case PERCENT -> (int)(price * this.discountValue / 100.0);
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Coupon that = (Coupon)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}