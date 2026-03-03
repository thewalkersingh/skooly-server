package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubjectRequest {
	@NotBlank(message = "Subject name is required")
	@Size(max = 100)
	private String name;
	
	@Size(max = 20)
	private String code;
	private String description;
}