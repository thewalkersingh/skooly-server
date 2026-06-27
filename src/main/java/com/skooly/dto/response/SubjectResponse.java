package com.skooly.dto.response;

import com.skooly.dto.ParentSummary;
import com.skooly.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponse {
	
	private Long id;
	private LocalDate dob;
	private LocalDate admissionDate;
	private String photoUrl;
	private AddressResponse address;
	private StudentStatus studentStatus;
	private String guardianName;
	private String guardianRelation;
	
	// Flat section fields — enough for list views
	private Long sectionId;
	private String sectionName;
	private String classroomName;   // reachable via section → classroom
	
	// Parent summary — null if not linked yet
	private ParentSummary parent;
	
	private UserIdentityResponse identity;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}