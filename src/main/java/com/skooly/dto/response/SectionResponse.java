package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponse {
	private Long id;
	private Long classId;
	private String className;
	private String name;
	private Long teacherId;
	private String teacherName;
	private LocalDateTime createdAt;
}