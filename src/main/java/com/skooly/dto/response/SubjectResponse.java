package com.skooly.dto.response;

import com.skooly.enums.SubjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectResponse {
	
	private Long id;
	private String subjectName;
	private String subjectCode;
	private SubjectStatus subjectStatus;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	List<TeacherResponse> teachers;
	
}