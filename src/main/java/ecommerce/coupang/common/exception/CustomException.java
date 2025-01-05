package ecommerce.coupang.common.exception;

import lombok.RequiredArgsConstructor;

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

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
