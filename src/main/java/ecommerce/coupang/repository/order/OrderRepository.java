package ecommerce.coupang.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.order.Order;

@LogLevel("OrderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * 회원 ID로 주문 목록 조회
	 * @param memberId 회원 ID
	 * @return 주문 목록
	 */
	@Query("select o from Order o "
		+ "join fetch o.address a "
		+ "where o.member.id = :memberId")
	List<Order> findByMemberIdWithAddress(Long memberId);
}
