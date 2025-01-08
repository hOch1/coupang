package ecommerce.coupang.service.order;

import org.springframework.stereotype.Component;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.dto.request.order.CreateOrderByProductRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderCreateManagement {

	public void createByProduct(Order order, CreateOrderByProductRequest request, Member member) {

	}
}
