package ecommerce.coupang.dto.response.store.statics;

import java.time.LocalDate;

import ecommerce.coupang.domain.store.statistics.DailySalesStatistics;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyStaticsResponse {

	private final Long id;
	private final Long storeId;
	private final int totalSalesCount;
	private final int totalSalesPrice;
	private final LocalDate date;

	public static DailyStaticsResponse of(DailySalesStatistics dailySalesStatistics) {
		return new DailyStaticsResponse(
			dailySalesStatistics.getId(),
			dailySalesStatistics.getStoreId(),
			dailySalesStatistics.getTotalSalesCount(),
			dailySalesStatistics.getTotalSalesPrice(),
			dailySalesStatistics.getDate()
		);
	}
}
