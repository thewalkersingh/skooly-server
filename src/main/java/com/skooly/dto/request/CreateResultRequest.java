package com.skooly.dto.request;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateResultRequest {
	@NotNull(message = "Exam ID is required")
	private Long examId;
	
	@NotNull(message = "Student ID is required")
	private Long studentId;
	
	@NotNull(message = "Marks obtained is required")
	@DecimalMin(value = "0.0")
	private BigDecimal marksObtained;
	private String remarks;
}