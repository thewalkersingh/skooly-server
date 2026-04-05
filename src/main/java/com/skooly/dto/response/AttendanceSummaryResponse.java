package com.skooly.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceSummaryResponse {
	private long total;
	private long present;
	private long absent;
	private long late;
	private double presentPercent;
}