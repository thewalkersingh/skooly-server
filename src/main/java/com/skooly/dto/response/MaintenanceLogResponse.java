package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogResponse {
	private Long id;
	private Long facilityId;
	private String facilityName;
	private String reportedBy;
	private String issue;
	private LocalDate reportedDate;
	private LocalDate resolvedDate;
	private String status;
	private LocalDateTime createdAt;
}