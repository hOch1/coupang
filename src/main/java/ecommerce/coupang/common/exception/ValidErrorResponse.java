package ecommerce.coupang.common.exception;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidErrorResponse {

	private final int status;
	private final Map<String, String> errors;
}
