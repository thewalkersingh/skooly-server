package com.skooly.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
	
	private boolean success;
	private String message;
	private T data;
	
	@Builder.Default
	private LocalDateTime timestamp = LocalDateTime.now();
	private Integer statusCode; // optional
	
}