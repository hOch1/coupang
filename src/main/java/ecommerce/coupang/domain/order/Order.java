package ecommerce.coupang.domain.order;

import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "orders")
public class Order extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment", nullable = false)
	private Payment payment;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus status;

	@Column(name = "total_price", nullable = false)
	private int totalPrice;

	@Column(name = "order_message")
	private String orderMessage;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	public Order(Member member, Address address, Payment payment, OrderStatus status, int totalPrice, String orderMessage,
		List<OrderItem> orderItems) {

		this.member = member;
		this.address = address;
		this.payment = payment;
		this.status = status;
		this.totalPrice = totalPrice;
		this.orderMessage = orderMessage;
		this.orderItems = orderItems;
	}
}
