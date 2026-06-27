package com.skooly.dto.request;

import com.skooly.enums.Gender;
import jakarta.validation.constraints.Email;
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
public class UserIdentityRequest {
	
	@NotBlank
	@Size(max = 100)
	private String firstName;
	
	@Size(max = 100)
	private String lastName;
	
	@NotBlank
	@Size(max = 30)
	private String phone;
	
	@Email
	@Size(max = 100)
	private String email;
	
	@NotNull
	private Gender gender;
	
}