package com.skooly.dto.request;

import com.skooly.enums.TeacherStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class TeacherRequest {
	
	@Size(max = 200)
	private String qualification;
	private Integer experience;
	
	@Size(max = 500)
	private String photoUrl;
	private LocalDate dob;
	private LocalDate joiningDate;
	private AddressRequest address;   // embedded DTO
	
	@NotNull
	private TeacherStatus teacherStatus;
	
	private List<Long> subjectIds;    // references to subjects
	
	@NotNull
	private UserIdentityRequest identity;  // nested DTO
	
	/*
	Subjects → here we’re just passing IDs in the request/response. In the service layer, you’ll fetch actual Subject
	entities by ID and attach them to the Teacher. MapStruct keeps DTOs clean.
	 */
}