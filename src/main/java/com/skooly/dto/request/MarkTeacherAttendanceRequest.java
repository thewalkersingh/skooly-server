package com.skooly.dto.request;
import com.skooly.model.TeacherAttendance;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkTeacherAttendanceRequest {
	@NotNull(message = "Teacher ID is required")
	private Long teacherId;
	
	@NotNull(message = "Date is required")
	private LocalDate date;
	
	@NotNull(message = "Status is required")
	private TeacherAttendance.AttendanceStatus status;
	private String remarks;
}