package com.skooly.exception;
import com.skooly.dto.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(404)
				       .body(new ApiResponse<>(false, ex.getMessage(), null));
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
		return ResponseEntity.status(400)
				       .body(new ApiResponse<>(false, ex.getMessage(), null));
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiResponse<?>> handleUnauthorized(UnauthorizedException ex) {
		return ResponseEntity.status(401)
				       .body(new ApiResponse<>(false, ex.getMessage(), null));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors()
				                 .stream().map(e -> e.getField()+": "+e.getDefaultMessage())
				                 .collect(Collectors.joining(", "));
		return ResponseEntity.status(400)
				       .body(new ApiResponse<>(false, message, null));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {
		return ResponseEntity.status(500)
				       .body(new ApiResponse<>(false, "Internal server error", null));
	}
}