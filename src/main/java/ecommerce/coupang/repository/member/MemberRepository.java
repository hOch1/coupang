package ecommerce.coupang.repository.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Member;

@LogLevel("MemberRepository")
public interface MemberRepository extends JpaRepository<Member, Long> {

	/**
	 * Email 로 회원 조회
	 * @param email 이메일
	 * @return 회원
	 */
	Optional<Member> findByEmail(String email);

	/**
	 * 이메일 중복 체크
	 * @param email 이메일
	 * @return boolean
	 */
	boolean existsByEmail(String email);

	/**
	 * 핸드폰 번호 중복 체크
	 * @param phoneNumber 핸드폰 번호
	 * @return boolean
	 */
	boolean existsByPhoneNumber(String phoneNumber);
}
