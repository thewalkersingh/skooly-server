package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponse {
	private Long id;
	private Long examId;
	private String examName;
	private Long studentId;
	private String studentName;
	private BigDecimal marksObtained;
	private BigDecimal totalMarks;
	private String grade;
	private String remarks;
	private String status;
	private LocalDateTime createdAt;
}