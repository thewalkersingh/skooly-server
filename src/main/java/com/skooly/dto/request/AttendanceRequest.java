package com.skooly.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AttendanceRequest {
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotNull(message = "Date is required")
	private LocalDate date;
	
	@NotNull(message = "Attendance records are required")
	private List<AttendanceEntry> records;
	
	@Data
	public static class AttendanceEntry {
		@NotNull
		private Long studentId;
		
		@NotNull
		private String status;  // PRESENT, ABSENT, LATE
		private String remarks;
		
	}
	
}