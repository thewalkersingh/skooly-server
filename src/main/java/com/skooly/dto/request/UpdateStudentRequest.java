package com.skooly.dto.request;
import com.skooly.model.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStudentRequest {
	@Size(max = 100)
	private String firstName;
	
	@Size(max = 100)
	private String lastName;
	private LocalDate dob;
	private Student.Gender gender;
	private String address;
	
	@Size(max = 20)
	private String phone;
	
	@Email
	private String email;
	private Long classId;
	private Long sectionId;
	private Long parentId;
}