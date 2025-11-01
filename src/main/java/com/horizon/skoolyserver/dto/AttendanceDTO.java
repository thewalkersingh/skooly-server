package com.horizon.skoolyserver.dto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceDTO {
	private Long id;
	private LocalDate date;
	private boolean present;
	private Long studentId;
}