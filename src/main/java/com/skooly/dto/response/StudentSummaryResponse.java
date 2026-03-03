package com.skooly.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSummaryResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String gender;
	private String className;
	private String sectionName;
	private String status;
	private String photo;
}