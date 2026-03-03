package com.skooly.dto.request;
import com.skooly.model.Attendance;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkAttendanceRequest {
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotNull(message = "Date is required")
	private LocalDate date;
	
	@NotEmpty(message = "Attendance entries cannot be empty")
	private List<AttendanceEntry> entries;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AttendanceEntry {
		@NotNull private Long studentId;
		
		@NotNull private Attendance.AttendanceStatus status;
		private String remarks;
	}
}