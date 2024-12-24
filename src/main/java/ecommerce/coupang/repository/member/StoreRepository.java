package ecommerce.coupang.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.member.Store;

@LogLevel("StoreRepository")
public interface StoreRepository extends JpaRepository<Store, Long> {

	/**
	 * 사업자 등록번호 중복 체크
	 * @param storeNumber 사업자 등록 번호
	 * @return boolean
	 */
	boolean existsByStoreNumber(String storeNumber);

	/**
	 * ID로 상점 조회
	 * @param storeId 상점 ID
	 * @return 상점
	 */
	@Query("select s from Store s "
		+ "join fetch s.member m "
		+ "where s.id = :storeId")
	Optional<Store> findByIdWithMember(Long storeId);

	/**
	 * 회원으로 상점 목록 조회
	 * @param memberId 회원 ID
	 * @return 상점 리스트
	 */
	@Query("select s from Store s "
		+ "join fetch s.member m "
		+ "where m.id = :memberId")
	List<Store> findByMemberId(Long memberId);
}
