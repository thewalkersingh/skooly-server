
package com.skooly.dto.request;
import com.skooly.model.Teacher;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTeacherRequest {
	@NotNull(message = "User ID is required")
	private Long userId;
	
	@NotBlank(message = "First name is required")
	@Size(max = 100)
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(max = 100)
	private String lastName;
	private LocalDate dob;
	private Teacher.Gender gender;
	private String address;
	
	@Size(max = 20)
	private String phone;
	
	@Email(message = "Invalid email format")
	private String email;
	private LocalDate joiningDate;
	private Long subjectId;
	
	@Size(max = 255)
	private String qualification;
	
	@Min(value = 0, message = "Experience cannot be negative")
	private Integer experience;
}