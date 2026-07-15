package com.skooly.dto.request;

import com.skooly.enums.ParentStatus;
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
public class ParentRequest {
	
	@Size(max = 200)
	private String occupation;
	
	@Size(max = 50)
	private String relation;
	private AddressRequest address;   // embedded DTO
	
	@NotNull
	private ParentStatus parentStatus;
	
	@NotNull
	private UserIdentityRequest identity;  // nested DTO
	
}