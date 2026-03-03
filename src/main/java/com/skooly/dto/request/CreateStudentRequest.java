package com.skooly.dto.request;
import com.skooly.model.Student;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStudentRequest {
	@NotBlank(message = "First name is required")
	@Size(max = 100)
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(max = 100)
	private String lastName;
	private LocalDate dob;
	private Student.Gender gender;
	private String address;
	
	@Size(max = 20)
	private String phone;
	
	@Email(message = "Invalid email format")
	private String email;
	private LocalDate admissionDate;
	
	@NotNull(message = "Class is required")
	private Long classId;
	
	@NotNull(message = "Section is required")
	private Long sectionId;
	private Long parentId;
}