package ecommerce.coupang.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

	private final int status;
	private final String message;
}
