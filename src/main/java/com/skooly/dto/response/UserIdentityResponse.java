package com.skooly.dto.response;

import com.skooly.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserIdentityResponse {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private Gender gender;
	
}