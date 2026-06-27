package com.skooly.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
	
	private String houseNumber;
	private String streetName;
	private String zipCode;
	private String city;
	private String state;
	
}