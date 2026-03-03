
package com.skooly.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherSummaryResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private String subjectName;
	private String qualification;
	private Integer experience;
	private String gender;
	private String photo;
	private String status;
}