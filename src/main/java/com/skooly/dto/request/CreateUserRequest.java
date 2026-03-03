package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
	@NotBlank(message = "Username is required")
	@Size(max = 100)
	private String username;
	
	@NotBlank(message = "Password is required")
	@Size(min = 6)
	private String password;
	
	@NotBlank(message = "Role is required")
	private String role;
	private Boolean isActive = true;
}