package com.skooly.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFeeStructureRequest {
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotNull(message = "Fee category ID is required")
	private Long feeCategoryId;
	
	@NotBlank(message = "Academic year is required")
	@Size(max = 20)
	private String academicYear;
	private LocalDate dueDate;
}