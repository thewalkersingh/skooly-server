package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SchoolClassRequest {
	@NotBlank(message = "Class name is required")
	private String name;
	private Integer gradeLevel;
}