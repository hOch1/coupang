package ecommerce.coupang.domain.store.statistics;

import java.time.LocalDate;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "total_sales_count", nullable = false)
	private int totalSalesCount;

	@Column(name = "total_sales_price", nullable = false)
	private int totalSalesPrice;

	@Column(name = "date", nullable = false)
	private LocalDate date;
}
