package com.skooly.dto.request.auth;

// Self registration — creates PENDING user awaiting admin approval

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
public class RegisterRequest {
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotBlank
	@Size(max = 30)
	private String phone;
	
	private String email;           // optional — phone is mandatory
	
	@NotNull
	private Gender gender;
	
	@NotNull
	private UserRole role;          // TEACHER, STUDENT, PARENT, STAFF
	
	@NotNull
	private Long schoolId;          // which school they belong to
	
	private Long roleEntityId;      // optional — link to existing Teacher/Student/etc record
	
}