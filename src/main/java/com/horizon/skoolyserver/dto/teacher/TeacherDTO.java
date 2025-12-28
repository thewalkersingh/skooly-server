package com.horizon.skoolyserver.dto.teacher;
import com.horizon.skoolyserver.constants.Subject;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TeacherDTO {
	private Long id;
	
	@NotBlank
	private String employeeCode;
	
	@NotBlank
	private String name;
	
	@Email
	private String email;
	private String phone;
	private Set<Subject> subjects = new HashSet<>();
}