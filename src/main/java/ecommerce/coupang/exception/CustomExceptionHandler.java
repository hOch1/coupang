package ecommerce.coupang.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> exception(CustomException e) {
		ErrorResponse response = new ErrorResponse(e.getStatus(), e.getMessage());
		return ResponseEntity.status(e.getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidErrorResponse> validException(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		ValidErrorResponse response = new ValidErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);
		return ResponseEntity.status(response.getStatus()).body(response);
	}
}
