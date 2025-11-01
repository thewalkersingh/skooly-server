package com.horizon.skoolyserver.dto;
import lombok.Data;

@Data
public class StudentDTO {
	private Long id;
	private String admissionNumber;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Long classId;   // reference to SchoolClass
	private Long parentId;  // reference to Parent
}