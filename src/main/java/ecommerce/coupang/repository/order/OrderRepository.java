package ecommerce.coupang.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;

@LogLevel("OrderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByMember(Member member);
}
