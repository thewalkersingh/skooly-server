package com.skooly.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
	
	@NotBlank
	private String identifier;      // email OR phone
	
	@NotBlank
	private String password;
	
	private String deviceFingerprint;   // optional — for new device detection
	
}