package com.horizon.skoolyserver.dto.school;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SchoolClassDTO {
	private Long id;
	
	@NotBlank
	private String grade;
	
	@NotBlank
	private String section;
	private Long teacherId;
}