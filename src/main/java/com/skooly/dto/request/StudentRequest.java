package com.skooly.dto.request;

import com.skooly.enums.StudentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRequest {
	
	@NotNull
	private LocalDate dob;
	private LocalDate admissionDate;
	
	@Size(max = 500)
	private String photoUrl;
	private AddressRequest address;   // embedded  DTO for address
	private String guardianName;
	private String guardianRelation;
	
	@NotNull
	private StudentStatus studentStatus;
	
	@NotNull
	private UserIdentityRequest identity;  // nested DTO
	
}