package ecommerce.coupang.domain.order;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.dto.request.delivery.AddDeliveryInfoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_status", nullable = false)
	private DeliveryStatus deliveryStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_company")
	private DeliveryCompany deliveryCompany;

	@Column(name = "code")
	private String code;

	public Delivery(OrderItem orderItem) {
		this.orderItem = orderItem;
		this.deliveryStatus = DeliveryStatus.PENDING;
	}

	public static Delivery from(OrderItem orderItem) {
		return new Delivery(orderItem);
	}

	public void setCompanyInfo(AddDeliveryInfoRequest request) {
		this.deliveryCompany = request.getDeliveryCompany();
		this.code = request.getCode();
	}

	public void updateStatus(DeliveryStatus status) {
		this.deliveryStatus = status;
	}

	public void cancel() throws CustomException {
		if (!deliveryStatus.equals(DeliveryStatus.PENDING))
			throw new CustomException(ErrorCode.ALREADY_DELIVERY_START);
	}
}
