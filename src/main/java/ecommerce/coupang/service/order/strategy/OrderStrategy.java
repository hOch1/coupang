package ecommerce.coupang.service.order.strategy;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.member.Address;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.dto.request.order.CreateOrderRequest;

public interface OrderStrategy<T extends CreateOrderRequest> {

	Order createOrder(T orderRequest, Member member, Address address) throws CustomException;
}
