package ecommerce.coupang.repository.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.order.Order;
import ecommerce.coupang.repository.order.custom.OrderCustomRepository;

@LogLevel("OrderRepository")
public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

	@Query("select o from Order o "
		+ "join fetch o.member m "
		+ "join fetch o.address a "
		+ "where o.id = :orderId")
	Optional<Order> findByIdWithMemberAndAddress(Long orderId);

	/**
	 * 회원 ID로 주문 목록 조회
	 * @param memberId 회원 ID
	 * @return 주문 목록
	 */
	@Query("select o from Order o "
		+ "join fetch o.member m "
		+ "join fetch o.address a "
		+ "where o.member.id = :memberId")
	List<Order> findByMemberIdWithAddress(Long memberId);
}
