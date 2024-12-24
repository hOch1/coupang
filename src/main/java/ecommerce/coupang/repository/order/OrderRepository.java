package ecommerce.coupang.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.order.Order;

@LogLevel("OrderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("select o from Order o "
		+ "join fetch o.address a "
		+ "where o.member = :member")
	List<Order> findByMemberWithAddress(Member member);
}
