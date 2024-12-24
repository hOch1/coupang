package ecommerce.coupang.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Address;

@LogLevel("AddressRepository")
public interface AddressRepository extends JpaRepository<Address, Long> {

	/**
	 * 회원 ID로 주소록 조회
	 * @param memberId 회원 ID
	 * @return 주소 목록
	 */
	List<Address> findByMemberId(Long memberId);

	/**
	 * 회원 ID로 기본 주소 조회
	 * @param memberId 회원 ID
	 * @return 기본 주소
	 */
	Optional<Address> findByMemberIdAndIsDefaultTrue(Long memberId);
}
