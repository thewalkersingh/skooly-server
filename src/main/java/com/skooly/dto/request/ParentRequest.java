package com.skooly.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
	private UserIdentityRequest identity;  // nested DTO
	private List<Long> studentIds;    // references to children
	
}