package ecommerce.coupang.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Address;

@LogLevel("AddressRepository")
public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByMemberId(Long memberId);

	Optional<Address> findByMemberIdAndIsDefaultTrue(Long id);
}
