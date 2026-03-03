package com.skooly.dto.request;
import com.skooly.model.Teacher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTeacherRequest {
	@Size(max = 100)
	private String firstName;
	
	@Size(max = 100)
	private String lastName;
	private LocalDate dob;
	private Teacher.Gender gender;
	private String address;
	
	@Size(max = 20)
	private String phone;
	
	@Email
	private String email;
	private LocalDate joiningDate;
	private Long subjectId;
	
	@Size(max = 255)
	private String qualification;
	
	@Min(0)
	private Integer experience;
}