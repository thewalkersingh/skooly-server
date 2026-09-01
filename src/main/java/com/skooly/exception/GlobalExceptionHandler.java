package com.skooly.exception;

import com.skooly.wrapper.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// ── Validation errors (@Valid failures) ───────────────────────────────────
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<Map<String, String>> handleValidationErrors(
		MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(field, message);
		});
		return ApiResponse.<Map<String, String>>builder()
								.success(false)
								.message("Validation failed")
								.data(errors)
								.statusCode(400)
								.build();
	}
	
	// ── Resource not found ────────────────────────────────────────────────────
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse<Void> handleResourceNotFound(ResourceNotFoundException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message(ex.getMessage())
								.statusCode(404)
								.build();
	}
	
	// ── Entity not found ────────────────────────────────────────────────────
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse<Void> handleEntityNotFound(EntityNotFoundException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message(ex.getMessage())
								.statusCode(404)
								.build();
	}
	
	// ── Auth errors ───────────────────────────────────────────────────────────
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse<Void> handleBadCredentials(BadCredentialsException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("Invalid email/phone or password")
								.statusCode(401)
								.build();
	}
	
	// ── Account locked ───────────────────────────────────────────────────────
	@ExceptionHandler(LockedException.class)
	@ResponseStatus(HttpStatus.LOCKED)
	public ApiResponse<Void> handleAccountLocked(LockedException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("Account is locked. Please contact your admin.")
								.statusCode(423)
								.build();
	}
	
	// ── Account disabled ─────────────────────────────────────────────────────
	@ExceptionHandler(DisabledException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse<Void> handleAccountDisabled(DisabledException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("Account is disabled. Please contact your admin.")
								.statusCode(403)
								.build();
	}
	
	// ── Username not found ───────────────────────────────────────────────────
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse<Void> handleUsernameNotFound(UsernameNotFoundException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("User not found")
								.statusCode(404)
								.build();
	}
	
	// ── Access denied ───────────────────────────────────────────────────────
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse<Void> handleAccessDenied(AccessDeniedException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("You do not have permission to perform this action")
								.statusCode(403)
								.build();
	}
	
	// ── JWT errors ────────────────────────────────────────────────────────────
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse<Void> handleExpiredJwt(ExpiredJwtException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("Token has expired. Please log in again.")
								.statusCode(401)
								.build();
	}
	
	// ── JWT errors ────────────────────────────────────────────────────────────
	@ExceptionHandler(MalformedJwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse<Void> handleMalformedJwt(MalformedJwtException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("Invalid token format")
								.statusCode(401)
								.build();
	}
	
	// ── JWT errors ────────────────────────────────────────────────────────────
	@ExceptionHandler(SignatureException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse<Void> handleSignatureException(SignatureException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message("Token signature is invalid")
								.statusCode(401)
								.build();
	}
	
	// ── Business logic errors ─────────────────────────────────────────────────
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse<Void> handleIllegalState(IllegalStateException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message(ex.getMessage())
								.statusCode(409)
								.build();
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiResponse<Void> handleIllegalArgument(IllegalArgumentException ex) {
		return ApiResponse.<Void>builder()
								.success(false)
								.message(ex.getMessage())
								.statusCode(400)
								.build();
	}
	
	// ── Fallback — catch everything else ──────────────────────────────────────
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse<Void> handleGeneric(Exception ex) {
		log.error("Unhandled exception: ", ex);
		return ApiResponse.<Void>builder()
								.success(false)
								.message("Something went wrong. Please try again later.")
								.statusCode(500)
								.build();
	}
	
}