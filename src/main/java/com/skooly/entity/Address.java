package com.skooly.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class Address {
	
	private String houseNumber;
	private String streetName;
	private String zipCode;
	private String city;
	private String state;
	
}