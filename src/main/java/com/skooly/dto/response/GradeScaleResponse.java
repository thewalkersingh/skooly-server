package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeScaleResponse {
	private Long id;
	private String grade;
	private BigDecimal minMarks;
	private BigDecimal maxMarks;
	private BigDecimal gpa;
}