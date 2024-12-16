package ecommerce.coupang.domain.order;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Store;
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
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Delivery extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item_id", nullable = false)
	private OrderItem orderItem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_status", nullable = false)
	private DeliveryStatus deliveryStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_company", nullable = false)
	private DeliveryCompany deliveryCompany;

	@Column(name = "code")
	private String code;

	public Delivery(OrderItem orderItem, Store store, DeliveryStatus deliveryStatus, DeliveryCompany deliveryCompany, String code) {
		this.orderItem = orderItem;
		this.store = store;
		this.deliveryStatus = deliveryStatus;
		this.deliveryCompany = deliveryCompany;
		this.code = code;
	}

	public static Delivery create() {

	}
}
