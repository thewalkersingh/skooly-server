package com.skooly.dto.response;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimetableResponse {
	private Long id;
	private Long classId;
	private String className;
	private Long sectionId;
	private String sectionName;
	private Long subjectId;
	private String subjectName;
	private Long teacherId;
	private String teacherName;
	private Long roomId;
	private String roomName;
	private String dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
}