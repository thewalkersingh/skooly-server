package com.skooly.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetPasswordRequest {
	
	@NotBlank
	private String identifier;      // email OR phone
	
	@NotBlank
	@Size(min = 8, max = 100)
	private String newPassword;
	
	@NotBlank
	private String confirmPassword;
	
	@NotBlank
	@Size(min = 6, max = 6)
	private String otp;             // must verify OTP before setting password
	
}