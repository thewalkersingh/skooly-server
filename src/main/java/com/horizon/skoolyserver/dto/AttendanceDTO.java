package com.horizon.skoolyserver.dto;
import com.horizon.skoolyserver.constants.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceDTO {
	private Long id;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private AttendanceStatus status;
	
	@NotNull
	private Long studentId;
}