package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDepartmentRequest {
	@NotBlank(message = "Department name is required")
	@Size(max = 100)
	private String name;
	private String description;
	private Long headId;
}