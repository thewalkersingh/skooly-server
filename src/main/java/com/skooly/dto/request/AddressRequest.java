package com.skooly.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {
	
	@Size(max = 50)
	private String houseNumber;
	
	@Size(max = 100)
	private String streetName;
	
	@Size(max = 20)
	private String zipCode;
	
	@Size(max = 100)
	private String city;
	
	@Size(max = 100)
	private String state;
	
}