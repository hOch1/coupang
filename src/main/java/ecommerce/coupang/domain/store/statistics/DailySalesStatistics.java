package ecommerce.coupang.domain.store.statistics;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DailySalesStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "store_id", nullable = false)
	private Long storeId;

	@Column(name = "total_sales_count", nullable = false)
	private int totalSalesCount;

	@Column(name = "total_sales_price", nullable = false)
	private int totalSalesPrice;

	@Column(name = "date", nullable = false)
	private LocalDate date;

	public DailySalesStatistics(Long storeId, int totalSalesCount, int totalSalesPrice, LocalDate date) {
		this.storeId = storeId;
		this.totalSalesCount = totalSalesCount;
		this.totalSalesPrice = totalSalesPrice;
		this.date = date;
	}

	public static DailySalesStatistics of(Long storeId, int totalSalesPrice, int totalSalesCount) {
		return new DailySalesStatistics(
			storeId,
			totalSalesPrice,
			totalSalesCount,
			LocalDate.now().minusDays(1)
		);
	}
}
