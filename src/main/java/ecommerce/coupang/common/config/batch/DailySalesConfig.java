package ecommerce.coupang.common.config.batch;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import ecommerce.coupang.domain.store.statistics.DailySalesStatistics;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DailySalesConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean
	public Job daliySalesJob(Step dailyStep) {
		return new JobBuilder("dailySalesJob", jobRepository)
			.start(dailyStep)
			.build();
	}

	@Bean
	public Step dailyStep(JdbcPagingItemReader<Map<String, Object>> reader,
		ItemProcessor<Map<String, Object>, DailySalesStatistics> processor,
		JdbcBatchItemWriter<DailySalesStatistics> writer) {

		return new StepBuilder("dailyStep", jobRepository)
			.<Map<String, Object>, DailySalesStatistics>chunk(500, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}

	@Bean
	public JdbcPagingItemReader<Map<String, Object>> salesDataReader(DataSource dataSource) {
		JdbcPagingItemReader<Map<String, Object>> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(500);
		reader.setRowMapper(new ColumnMapRowMapper());

		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause(
			"select s.store_id AS store_id, " +
			"COALESCE(SUM(o.totalPrice), 0) AS totalSalesPrice, " +
			"COALESCE(COUNT(oi.order_item_id), 0) AS totalSalesCount"
		);
		provider.setFromClause(
			"FROM Store s " +
			"LEFT JOIN Product p ON s.store_id = p.store_id " +
			"LEFT JOIN ProductVariant pv ON p.product_id = pv.product_id " +
			"LEFT JOIN OrderItem oi ON pv.product_variant_id = oi.product_variant_id " +
			"LEFT JOIN ("
				+ "SELECT order_id, total_price "
				+ "FROM Orders "
				+ "WHERE DATE(created_at) = CURDATE() - INTERVAL 1 DAY "
				+ "AND order_status != 'CANCELED' "
				+ ") o ON oi.order_id = o.order_id"
		);
		provider.setGroupClause("GROUP BY s.store_id");
		provider.setSortKeys(Map.of("s.store_id", org.springframework.batch.item.database.Order.ASCENDING));

		reader.setQueryProvider(provider);
		return reader;
	}

	@Bean
	public ItemProcessor<Map<String, Object>, DailySalesStatistics> dailySalesProcessor() {
		return item -> {
			Long storeId = (Long) item.get("store_id");
			int totalSalesPrice = (int) item.get("totalSalesPrice");
			int totalSalesCount = (int) item.get("totalSalesCount");

			return DailySalesStatistics.of(storeId, totalSalesPrice, totalSalesCount);
		};
	}

	@Bean
	public JdbcBatchItemWriter<DailySalesStatistics> dailySalesWriter(DataSource dataSource) {
		JdbcBatchItemWriter<DailySalesStatistics> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql(
			"INSERT INTO daily_sales_statistics (store_id, total_sales_price, total_sales_count) "
			+ "VALUES (:storeId, :totalSalesPrice, :totalSalesCount, :date)"
		);

		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		return writer;
	}
}
