package com.skooly.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryResponse {
	private Long studentId;
	private String studentName;
	private long totalDays;
	private long presentDays;
	private long absentDays;
	private double attendancePercentage;
}