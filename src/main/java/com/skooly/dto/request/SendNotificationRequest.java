package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationRequest {
	@NotEmpty(message = "At least one user ID is required")
	private List<Long> userIds;
	
	@NotBlank(message = "Title is required")
	@Size(max = 255)
	private String title;
	
	@NotBlank(message = "Message is required")
	private String message;
}