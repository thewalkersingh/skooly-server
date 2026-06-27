package com.skooly.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectSummary {
	
	private Long id;
	private String subjectName;
	private String subjectCode;
	
}