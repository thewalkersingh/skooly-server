package com.skooly.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeScaleRequest {
	@NotBlank
	@Size(max = 5)
	private String grade;
	
	@NotNull @DecimalMin("0.0")
	private BigDecimal minMarks;
	
	@NotNull @DecimalMin("0.0")
	private BigDecimal maxMarks;
	private BigDecimal gpa;
}