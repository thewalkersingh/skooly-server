package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherAttendanceResponse {
	private Long id;
	private Long teacherId;
	private String teacherName;
	private LocalDate date;
	private String status;
	private String remarks;
}