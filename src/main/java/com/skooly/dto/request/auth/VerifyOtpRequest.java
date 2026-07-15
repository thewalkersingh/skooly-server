package com.skooly.dto.request.auth;

import com.skooly.enums.OtpPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpRequest {
	
	@NotBlank
	private String identifier;      // email OR phone
	
	@NotBlank
	@Size(min = 6, max = 6)
	private String otp;
	
	@NotNull
	private OtpPurpose purpose;
	
}