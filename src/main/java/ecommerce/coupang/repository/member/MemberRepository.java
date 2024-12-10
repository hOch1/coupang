package ecommerce.coupang.repository.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;

@LogLevel("MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	boolean existsByEmail(String email);
	boolean existsByPhoneNumber(String phoneNumber);
}
