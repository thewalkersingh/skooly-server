package com.skooly.dto.response;

import com.skooly.dto.common.SubjectSummary;
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
public class SectionResponse {
	
	private Long id;
	private String sectionName;
	private Integer capacity;
	
	// Flat classroom fields — client doesn't need to call /classrooms/{id} just to show the name
	private Long classroomId;
	private String classroomName;    // add this
	
	// Flat teacher fields — enough to show assigned teacher info
	private Long teacherId;
	private String teacherName;      // add this (firstName + lastName from identity)
	
	// Subject summaries — not just IDs
	private List<SubjectSummary> subjects;   // add this
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}