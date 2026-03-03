package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResponse {
	private Long id;
	private String name;
	private Long classId;
	private String className;
	private Long subjectId;
	private String subjectName;
	private LocalDate examDate;
	private BigDecimal totalMarks;
	private BigDecimal passingMarks;
	private String academicYear;
	private LocalDateTime createdAt;
}