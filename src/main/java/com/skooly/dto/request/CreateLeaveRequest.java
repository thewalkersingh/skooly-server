package com.skooly.dto.request;
import com.skooly.model.LeaveRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLeaveRequest {
	@NotNull(message = "Staff ID is required")
	private Long staffId;
	
	@NotNull(message = "Leave type is required")
	private LeaveRequest.LeaveType leaveType;
	
	@NotNull(message = "From date is required")
	private LocalDate fromDate;
	
	@NotNull(message = "To date is required")
	private LocalDate toDate;
	private String reason;
}