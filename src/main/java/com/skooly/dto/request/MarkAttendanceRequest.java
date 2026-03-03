package com.skooly.dto.request;
import com.skooly.model.Attendance;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkAttendanceRequest {
	@NotNull(message = "Student ID is required")
	private Long studentId;
	
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotNull(message = "Date is required")
	private LocalDate date;
	
	@NotNull(message = "Status is required")
	private Attendance.AttendanceStatus status;
	private String remarks;
}