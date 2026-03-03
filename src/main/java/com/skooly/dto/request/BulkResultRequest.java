package com.skooly.dto.request;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkResultRequest {
	@NotNull(message = "Exam ID is required")
	private Long examId;
	
	@NotEmpty(message = "Results cannot be empty")
	private List<ResultEntry> results;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ResultEntry {
		@NotNull private Long studentId;
		
		@NotNull @DecimalMin("0.0") private BigDecimal marksObtained;
		private String remarks;
	}
}