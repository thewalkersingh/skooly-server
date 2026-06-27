package com.skooly.dto.response;

import com.skooly.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponse {
	
	private Long id;
	private LocalDate dob;
	private LocalDate admissionDate;
	private String photoUrl;
	private AddressResponse address; // embedded DTO
	private StudentStatus studentStatus;
	private Long sectionId;
	private Long parentId;
	private List<Long> subjectIds;
	private UserIdentityResponse identity;  // nested DTO
	
}