package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeStructureResponse {
	private Long id;
	private Long classId;
	private String className;
	private Long feeCategoryId;
	private String feeCategoryName;
	private String academicYear;
	private LocalDate dueDate;
	private LocalDateTime createdAt;
}