package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSectionRequest {
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotBlank(message = "Section name is required")
	@Size(max = 50)
	private String name;
	private Long teacherId;
}