package com.skooly.dto.response;

import com.skooly.enums.ClassroomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassroomResponse {
	
	private Long id;
	private String classroomName;
	private String classroomCode;
	private ClassroomStatus classroomStatus;
	private Long schoolId;   // reference back to School
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}