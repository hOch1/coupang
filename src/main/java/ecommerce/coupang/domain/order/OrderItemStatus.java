package ecommerce.coupang.domain.order;

public enum OrderItemStatus {
	ORDERED,    // 주문 생성됨
	CANCEL,     // 이 아이템만 취소
	REFUND,     // 환불
	DELIVERED,  // 개별 배송 완료
}
