package com.skooly.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffSummaryResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String departmentName;
	private String designation;
	private String gender;
	private String photo;
	private String status;
}