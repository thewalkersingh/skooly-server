package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMaintenanceLogRequest {
	@NotNull(message = "Facility ID is required")
	private Long facilityId;
	
	@NotBlank(message = "Issue description is required")
	private String issue;
	
	@NotNull(message = "Reported date is required")
	private LocalDate reportedDate;
}