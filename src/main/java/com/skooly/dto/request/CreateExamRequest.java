package com.skooly.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateExamRequest {
	@NotBlank(message = "Exam name is required")
	@Size(max = 150)
	private String name;
	
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotNull(message = "Subject ID is required")
	private Long subjectId;
	
	@NotNull(message = "Exam date is required")
	private LocalDate examDate;
	
	@NotNull(message = "Total marks is required")
	@DecimalMin(value = "1.0")
	private BigDecimal totalMarks;
	
	@NotNull(message = "Passing marks is required")
	@DecimalMin(value = "1.0")
	private BigDecimal passingMarks;
	
	@NotBlank(message = "Academic year is required")
	private String academicYear;
}