package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {
	private Long id;
	private Long studentId;
	private String studentName;
	private Long classId;
	private String className;
	private LocalDate date;
	private String status;
	private String markedBy;
	private String remarks;
}