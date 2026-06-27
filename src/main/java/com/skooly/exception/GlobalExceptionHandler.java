package com.skooly.exception;

import com.skooly.wrapper.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse<Void> handleNotFound(ResourceNotFoundException ex) {
		return ApiResponse.<Void>builder()
			        .success(false)
			        .message(ex.getMessage())
			        .statusCode(404)
			        .build();
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				       .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError) error).getField();
			errors.put(field, error.getDefaultMessage());
		});
		return ResponseEntity.badRequest()
				       .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors));
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // sets HTTP 500
	public ApiResponse<Void> handleGeneric(Exception ex) {
		return ApiResponse.<Void>builder()
			        .success(false)
			        .message("Something went wrong")
			        .statusCode(500)
			        .build();
	}
	
	public record ErrorResponse(int status, String message, Map<String, String> validationErrors) {
	}
}