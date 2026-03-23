package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SectionRequest {
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotBlank(message = "Section name is required")
	private String name;
	private Integer capacity;
	private Long teacherId;
}