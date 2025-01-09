package ecommerce.coupang.domain.order;

import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.dto.request.order.CreateOrderByCartRequest;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
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
	@Column(name = "order_id")
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

	public Order(Member member, Address address, Payment payment, OrderStatus status, String orderMessage) {
		this.member = member;
		this.address = address;
		this.payment = payment;
		this.status = status;
		this.orderMessage = orderMessage;
	}

	/* 상품 주문 */
	public static Order createByProduct(CreateOrderByProductRequest request, Member member, Address address) {
		return new Order(
			member,
			address,
			request.getPayment(),
			(request.getPayment().equals(Payment.TRANSFER) ? OrderStatus.PENDING : OrderStatus.PAID),
			request.getOrderMessage()
		);
	}

	/* 장바구니 주문 */
	public static Order createByCart(CreateOrderByCartRequest request, Member member, Address address) {
		return new Order(
			member,
			address,
			request.getPayment(),
			(request.getPayment().equals(Payment.TRANSFER) ? OrderStatus.PENDING : OrderStatus.PAID),
			request.getOrderMessage()
		);
	}

	/* 주문 상품 추가 */
	public void addOrderItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
		this.totalPrice += orderItem.getTotalPrice();
	}

	/* 주문 취소 */
	public void cancel() throws CustomException {
		for (OrderItem orderItem : this.orderItems) {
			// 배송 대기중이 아닐경우 예외
			if (!orderItem.getDelivery().getDeliveryStatus().equals(DeliveryStatus.PENDING))
				throw new CustomException(ErrorCode.ALREADY_DELIVERY_START);

			validateNotCancelled();

			orderItem.cancel();
		}

		this.status = OrderStatus.CANCELLED;
	}

	public boolean isCancelled() {
		return this.status.equals(OrderStatus.CANCELLED);
	}

	public void validateNotCancelled() throws CustomException {
		if (isCancelled()) {
			throw new CustomException(ErrorCode.ALREADY_CANCELLED_ORDER);
		}
	}

	public void validateOrderOwner(Member member) throws CustomException {
		if (!this.getMember().equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}
}
