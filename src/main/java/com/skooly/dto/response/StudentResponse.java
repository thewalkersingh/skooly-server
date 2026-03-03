package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
	private Long id;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String gender;
	private String address;
	private String phone;
	private String email;
	private LocalDate admissionDate;
	private Long classId;
	private String className;
	private Long sectionId;
	private String sectionName;
	private Long parentId;
	private String photo;
	private String status;
	private LocalDateTime createdAt;
}