package com.skooly.dto.request.auth;

// ── CreateAccountRequest ──────────────────────────────────────────────────────
// Admin creates account directly — ACTIVE immediately

import com.skooly.enums.Gender;
import com.skooly.enums.UserRole;
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
public class CreateAccountRequest {
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotBlank
	@Size(max = 30)
	private String phone;
	
	private String email;
	
	@NotNull
	private Gender gender;
	
	@NotNull
	private UserRole userRole;
	
	@NotNull
	private Long schoolId;
	
	@NotNull
	private Long roleEntityId;      // required — admin must link to existing entity
	
}