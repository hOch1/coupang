package ecommerce.coupang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableRetry
public class CoupangApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoupangApplication.class, args);
	}
}
