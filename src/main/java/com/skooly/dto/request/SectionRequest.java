package com.skooly.dto.request;

import com.skooly.enums.SchoolStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionRequest {
	
	@NotBlank                    // NotNull won't catch empty strings, use NotBlank for Strings
	@Size(max = 5)               // sectionName is "A","B","C" — max 20 is too generous
	private String sectionName;
	
	@NotNull
	@Min(1)                      // capacity can't be 0 or negative
	@Max(200)                    // reasonable upper bound
	private Integer capacity;
	
	// REMOVED: classroomId — comes from path variable, not request body
	private SchoolStatus schoolStatus;
	private Long teacherId;          // optional
	private List<Long> subjectIds;   // optional
	
}