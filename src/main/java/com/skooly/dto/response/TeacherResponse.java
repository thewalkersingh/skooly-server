package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponse {
	private Long id;
	private Long userId;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String gender;
	private String address;
	private String phone;
	private String email;
	private LocalDate joiningDate;
	private Long subjectId;
	private String subjectName;
	private String qualification;
	private Integer experience;
	private String photo;
	private String status;
	private LocalDateTime createdAt;
}