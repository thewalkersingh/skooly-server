package com.horizon.skoolyserver.dto;
import lombok.Data;

@Data
public class SchoolClassDTO {
	private Long id;
	private String grade;
	private String section;
	private Long teacherId;
}