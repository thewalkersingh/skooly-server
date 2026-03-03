package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
	private Long id;
	private Long userId;
	private String title;
	private String message;
	private Boolean isRead;
	private LocalDateTime createdAt;
}