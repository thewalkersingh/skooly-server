package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestResponse {
	private Long id;
	private Long staffId;
	private String staffName;
	private String leaveType;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String reason;
	private String status;
	private String approvedBy;
	private LocalDateTime createdAt;
}