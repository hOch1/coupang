package ecommerce.coupang.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.domain.member.MemberCoupon;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

	@Query("select mc from MemberCoupon mc "
		+ "join fetch mc.coupon c "
		+ "where mc.id = :memberId "
		+ "order by mc.createdAt desc ")
	List<MemberCoupon> findByMemberId(Long memberId);

	boolean existsByCouponId(Long CouponId);

	Optional<MemberCoupon> findByMemberIdAndCouponId(Long memberId, Long couponId);
}
