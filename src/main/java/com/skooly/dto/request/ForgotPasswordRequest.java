package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordRequest {
	@NotBlank(message = "Username is required")
	private String username;
}