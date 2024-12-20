package ecommerce.coupang.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Store;

@LogLevel("StoreRepository")
public interface StoreRepository extends JpaRepository<Store, Long> {

	boolean existsByStoreNumber(String storeNumber);

	@Query("select s from Store s "
		+ "join fetch s.member m "
		+ "where s.id = :storeId")
	Optional<Store> findById(Long storeId);

	@Query("select s from Store s "
		+ "join fetch s.member m "
		+ "where m.id = :memberId")
	List<Store> findByMemberId(Long memberId);
}
