package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogResponse {
	private Long id;
	private Long userId;
	private String username;
	private String action;
	private String module;
	private String description;
	private String ipAddress;
	private LocalDateTime createdAt;
}