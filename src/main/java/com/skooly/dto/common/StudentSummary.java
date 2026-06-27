package com.skooly.dto.common;

import com.skooly.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentSummary {
	
	private Long id;
	private String studentName;    // firstName + lastName from identity
	private String phone;
	private StudentStatus studentStatus;
	private String sectionName;
	private String classroomName;
	
}