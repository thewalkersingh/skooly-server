package com.skooly.dto.request;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkTimetableRequest {
	@NotEmpty(message = "Timetable entries cannot be empty")
	private List<CreateTimetableRequest> entries;
}