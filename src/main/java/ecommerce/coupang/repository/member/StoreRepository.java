package ecommerce.coupang.repository.member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Store;

@LogLevel("StoreRepository")
public interface StoreRepository extends JpaRepository<Store, Long> {

	boolean existsByStoreNumber(String storeNumber);

	List<Store> findByMemberId(Long memberId);
}
