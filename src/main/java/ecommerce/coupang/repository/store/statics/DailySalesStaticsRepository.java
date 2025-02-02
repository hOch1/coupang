package ecommerce.coupang.repository.store.statics;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.store.statistics.DailySalesStatistics;

public interface DailySalesStaticsRepository extends JpaRepository<DailySalesStatistics, Long> {

	Optional<DailySalesStatistics> findByStoreIdAndDate(Long storeId, LocalDate date);
}
