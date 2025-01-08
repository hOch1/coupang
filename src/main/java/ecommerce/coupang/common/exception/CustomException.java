package ecommerce.coupang.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends Exception{

	private final ErrorCode errorCode;

	@Override
	public String getMessage() {
		return errorCode.getMessage();
	}

	public int getStatus() {
		return errorCode.getErrorStatus();
	}

	public String getError() {
		return errorCode.name();
	}
}
