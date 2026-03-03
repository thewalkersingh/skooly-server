package com.skooly.dto.request;
import com.skooly.model.Timetable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTimetableRequest {
	@NotNull(message = "Class ID is required")
	private Long classId;
	
	@NotNull(message = "Section ID is required")
	private Long sectionId;
	
	@NotNull(message = "Subject ID is required")
	private Long subjectId;
	
	@NotNull(message = "Teacher ID is required")
	private Long teacherId;
	private Long roomId;
	
	@NotNull(message = "Day of week is required")
	private Timetable.DayOfWeek dayOfWeek;
	
	@NotNull(message = "Start time is required")
	private LocalTime startTime;
	
	@NotNull(message = "End time is required")
	private LocalTime endTime;
}