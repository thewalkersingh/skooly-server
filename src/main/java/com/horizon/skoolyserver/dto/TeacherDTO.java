package com.horizon.skoolyserver.dto;
import lombok.Data;

@Data
public class TeacherDTO {
	private Long id;
	private String employeeCode;
	private String name;
	private String email;
	private String phone;
}