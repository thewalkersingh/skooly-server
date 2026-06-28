package com.skooly.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResendOtpRequest {
	
	@NotBlank
	private String identifier;      // email OR phone
	
	@NotNull
	private com.skooly.enums.OtpPurpose purpose;
	
}