package com.skooly.dto.request;

import com.skooly.enums.SubjectStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectRequest {
	
	@NotNull
	@Size(max = 100)
	private String subjectName;
	
	@NotNull
	@Size(max = 50)
	private String subjectCode;
	private Long sectionId;
	
	@NotNull
	private SubjectStatus status;
	
}