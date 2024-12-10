package ecommerce.coupang.aop.log;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
public class RequestUUID {

	private final String requestId;

	public RequestUUID() {
		String uuid = UUID.randomUUID().toString();
		requestId = uuid.substring(0, 8);
	}
}
