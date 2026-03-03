package com.skooly.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClassRequest {
	@NotBlank(message = "Class name is required")
	@Size(max = 100)
	private String name;
	
	@NotNull(message = "Grade level is required")
	@Min(value = 1, message = "Grade level must be at least 1")
	private Integer gradeLevel;
}